/*
 * Copyright Consensys Software Inc., 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.ethereum.executionlayer;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import org.apache.tuweni.units.bigints.UInt256;
import tech.pegasys.teku.ethereum.events.SlotEventsChannel;
import tech.pegasys.teku.ethereum.performance.trackers.BlockProductionPerformance;
import tech.pegasys.teku.infrastructure.async.SafeFuture;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.datastructures.blocks.SignedBeaconBlock;
import tech.pegasys.teku.spec.datastructures.builder.BuilderPayload;
import tech.pegasys.teku.spec.datastructures.execution.BlobsBundle;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayload;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayloadContext;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayloadResult;
import tech.pegasys.teku.spec.datastructures.execution.GetPayloadResponse;
import tech.pegasys.teku.spec.datastructures.execution.HeaderWithFallbackData;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.BeaconState;
import tech.pegasys.teku.spec.executionlayer.ExecutionLayerBlockProductionManager;
import tech.pegasys.teku.spec.executionlayer.ExecutionLayerChannel;

public class ExecutionLayerBlockProductionManagerImpl
    implements ExecutionLayerBlockProductionManager, SlotEventsChannel {

  private static final UInt64 EXECUTION_RESULT_CACHE_RETENTION_SLOTS = UInt64.valueOf(2);
  private static final UInt64 BUILDER_RESULT_CACHE_RETENTION_SLOTS = UInt64.valueOf(2);

  private final NavigableMap<UInt64, ExecutionPayloadResult> executionResultCache =
      new ConcurrentSkipListMap<>();

  private final NavigableMap<UInt64, BuilderPayload> builderResultCache =
      new ConcurrentSkipListMap<>();

  private final ExecutionLayerChannel executionLayerChannel;

  public ExecutionLayerBlockProductionManagerImpl(
      final ExecutionLayerChannel executionLayerChannel) {
    this.executionLayerChannel = executionLayerChannel;
  }

  @Override
  public void onSlot(final UInt64 slot) {
    executionResultCache
        .headMap(slot.minusMinZero(EXECUTION_RESULT_CACHE_RETENTION_SLOTS), false)
        .clear();
    builderResultCache
        .headMap(slot.minusMinZero(BUILDER_RESULT_CACHE_RETENTION_SLOTS), false)
        .clear();
  }

  @Override
  public Optional<ExecutionPayloadResult> getCachedPayloadResult(final UInt64 slot) {
    return Optional.ofNullable(executionResultCache.get(slot));
  }

  @Override
  public ExecutionPayloadResult initiateBlockProduction(
      final ExecutionPayloadContext context,
      final BeaconState blockSlotState,
      final boolean isBlind,
      final Optional<UInt64> requestedBuilderBoostFactor,
      final BlockProductionPerformance blockProductionPerformance) {
    final ExecutionPayloadResult result;
    if (!isBlind) {
      final SafeFuture<GetPayloadResponse> getPayloadResponseFuture =
          executionLayerChannel
              .engineGetPayload(context, blockSlotState)
              .thenPeek(__ -> blockProductionPerformance.engineGetPayload());
      final SafeFuture<ExecutionPayload> executionPayloadFuture =
          getPayloadResponseFuture.thenApply(GetPayloadResponse::getExecutionPayload);
      final SafeFuture<UInt256> executionPayloadValueFuture =
          getPayloadResponseFuture.thenApply(GetPayloadResponse::getExecutionPayloadValue);
      result =
          new ExecutionPayloadResult(
              context,
              Optional.of(executionPayloadFuture),
              Optional.empty(),
              Optional.empty(),
              Optional.of(executionPayloadValueFuture));
    } else {
      result =
          builderGetHeader(
              context, blockSlotState, requestedBuilderBoostFactor, blockProductionPerformance);
    }
    executionResultCache.put(blockSlotState.getSlot(), result);
    return result;
  }

  @Override
  public ExecutionPayloadResult initiateBlockAndBlobsProduction(
      final ExecutionPayloadContext context,
      final BeaconState blockSlotState,
      final boolean isBlind,
      final Optional<UInt64> requestedBuilderBoostFactor,
      final BlockProductionPerformance blockProductionPerformance) {
    final ExecutionPayloadResult result;
    if (!isBlind) {
      final SafeFuture<GetPayloadResponse> getPayloadResponseFuture =
          executionLayerChannel
              .engineGetPayload(context, blockSlotState)
              .thenPeek(__ -> blockProductionPerformance.engineGetPayload());
      final SafeFuture<ExecutionPayload> executionPayloadFuture =
          getPayloadResponseFuture.thenApply(GetPayloadResponse::getExecutionPayload);
      final SafeFuture<Optional<BlobsBundle>> blobsBundleFuture =
          getPayloadResponseFuture.thenApply(GetPayloadResponse::getBlobsBundle);
      final SafeFuture<UInt256> executionPayloadValueFuture =
          getPayloadResponseFuture.thenApply(GetPayloadResponse::getExecutionPayloadValue);
      result =
          new ExecutionPayloadResult(
              context,
              Optional.of(executionPayloadFuture),
              Optional.of(blobsBundleFuture),
              Optional.empty(),
              Optional.of(executionPayloadValueFuture));
    } else {
      result =
          builderGetHeader(
              context, blockSlotState, requestedBuilderBoostFactor, blockProductionPerformance);
    }
    executionResultCache.put(blockSlotState.getSlot(), result);
    return result;
  }

  @Override
  public SafeFuture<BuilderPayload> getUnblindedPayload(final SignedBeaconBlock signedBeaconBlock) {
    return executionLayerChannel
        .builderGetPayload(signedBeaconBlock, this::getCachedPayloadResult)
        .thenPeek(
            builderPayload -> builderResultCache.put(signedBeaconBlock.getSlot(), builderPayload));
  }

  @Override
  public Optional<BuilderPayload> getCachedUnblindedPayload(final UInt64 slot) {
    return Optional.ofNullable(builderResultCache.get(slot));
  }

  private ExecutionPayloadResult builderGetHeader(
      final ExecutionPayloadContext executionPayloadContext,
      final BeaconState state,
      final Optional<UInt64> requestedBuilderBoostFactor,
      final BlockProductionPerformance blockProductionPerformance) {

    final SafeFuture<UInt256> executionPayloadValueFuture = new SafeFuture<>();

    final SafeFuture<HeaderWithFallbackData> headerWithFallbackDataFuture =
        executionLayerChannel
            .builderGetHeader(
                executionPayloadContext,
                state,
                executionPayloadValueFuture,
                requestedBuilderBoostFactor,
                blockProductionPerformance)
            .whenException(executionPayloadValueFuture::completeExceptionally);

    return new ExecutionPayloadResult(
        executionPayloadContext,
        Optional.empty(),
        Optional.empty(),
        Optional.of(headerWithFallbackDataFuture),
        Optional.of(executionPayloadValueFuture));
  }
}
