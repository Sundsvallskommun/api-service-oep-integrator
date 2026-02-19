package se.sundsvall.oepintegrator.util.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InstanceTypeTest {

	@Test
	void enumValues() {
		assertThat(InstanceType.values()).containsExactlyInAnyOrder(
			InstanceType.EXTERNAL,
			InstanceType.INTERNAL);
	}

	@Test
	void enumToString() {
		assertThat(InstanceType.EXTERNAL).hasToString("EXTERNAL");
		assertThat(InstanceType.INTERNAL).hasToString("INTERNAL");
	}

}
