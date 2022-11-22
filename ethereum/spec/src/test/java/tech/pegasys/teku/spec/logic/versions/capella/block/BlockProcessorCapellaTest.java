/*
 * Copyright ConsenSys Software Inc., 2022
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

package tech.pegasys.teku.spec.logic.versions.capella.block;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.apache.tuweni.bytes.Bytes32;
import org.junit.jupiter.api.Test;
import tech.pegasys.teku.bls.BLSSignatureVerifier;
import tech.pegasys.teku.infrastructure.bytes.Bytes20;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.Spec;
import tech.pegasys.teku.spec.TestSpecFactory;
import tech.pegasys.teku.spec.datastructures.blocks.SignedBeaconBlock;
import tech.pegasys.teku.spec.datastructures.execution.versions.capella.Withdrawal;
import tech.pegasys.teku.spec.datastructures.state.Validator;
import tech.pegasys.teku.spec.datastructures.state.beaconstate.BeaconState;
import tech.pegasys.teku.spec.logic.common.statetransition.exceptions.StateTransitionException;
import tech.pegasys.teku.spec.logic.versions.bellatrix.BlockProcessorBellatrixTest;
import tech.pegasys.teku.spec.util.DataStructureUtil;

public class BlockProcessorCapellaTest extends BlockProcessorBellatrixTest {
  @Override
  protected Spec createSpec() {
    return TestSpecFactory.createMainnetCapella();
  }

  @Test
  void shouldRejectBellatrixBlock() {
    final DataStructureUtil data = new DataStructureUtil(TestSpecFactory.createMinimalBellatrix());
    BeaconState preState = createBeaconState();
    final SignedBeaconBlock block = data.randomSignedBeaconBlock(preState.getSlot().increment());
    assertThatThrownBy(
            () -> spec.processBlock(preState, block, BLSSignatureVerifier.SIMPLE, Optional.empty()))
        .isInstanceOf(StateTransitionException.class);
  }

  @Test
  void shouldCreateExpectedWithdrawalAddress() {
    final DataStructureUtil data = new DataStructureUtil(TestSpecFactory.createMinimalCapella());
    Bytes20 eth1Address = data.randomBytes20();

    Bytes32 bytes32 = BlockProcessorCapella.getWithdrawalAddressFromEth1Address(eth1Address);
    // ends with eth1 address
    assertThat(bytes32.toHexString()).endsWith(eth1Address.toUnprefixedHexString());
    // starts with 0x01 (eth1 prefix) and 0x00 x 11 (buffer)
    assertThat(bytes32.toHexString()).startsWith("0x010000000000000000000000");
  }

  @Test
  void shouldFindPartialWithdrawals() {
    final DataStructureUtil data = new DataStructureUtil(TestSpecFactory.createMinimalCapella());
    Validator validator =
        makeValidator(data.randomPublicKey(), data.randomEth1WithdrawalCredentials());
    BeaconState preState =
        createBeaconState(
            true, spec.getGenesisSpecConfig().getMaxEffectiveBalance().plus(1024000), validator);
    final Optional<List<Withdrawal>> withdrawals =
        spec.getBlockProcessor(preState.getSlot()).getExpectedWithdrawals(preState);
    assertThat(withdrawals.get().get(0).getAmount()).isEqualTo(UInt64.valueOf(1024000));
  }

  @Test
  void shouldFindFullWithdrawals() {
    final DataStructureUtil data = new DataStructureUtil(TestSpecFactory.createMinimalCapella());
    Validator validator =
        makeValidator(
            data.randomPublicKey(),
            data.randomEth1WithdrawalCredentials(),
            UInt64.ZERO,
            UInt64.ZERO);
    final UInt64 balance = spec.getGenesisSpecConfig().getMaxEffectiveBalance().plus(1024000);
    BeaconState preState = createBeaconState(true, balance, validator);
    final Optional<List<Withdrawal>> withdrawals =
        spec.getBlockProcessor(preState.getSlot()).getExpectedWithdrawals(preState);
    assertThat(withdrawals.get().get(0).getAmount()).isEqualTo(balance);
  }

  @Test
  void shouldIncrementIndexCorrectly() {
    assertThat(BlockProcessorCapella.incrementValidatorIndex(9, 10)).isEqualTo(0);
    assertThat(BlockProcessorCapella.incrementValidatorIndex(8, 10)).isEqualTo(9);
    assertThat(BlockProcessorCapella.incrementValidatorIndex(0, 10)).isEqualTo(1);
  }
}
