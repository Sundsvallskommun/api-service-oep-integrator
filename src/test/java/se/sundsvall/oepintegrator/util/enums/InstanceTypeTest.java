package se.sundsvall.oepintegrator.util.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
