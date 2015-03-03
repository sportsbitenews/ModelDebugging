package fr.inria.diverse.tracemm.semdiff.eval;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.inria.diverse.tracemm.semdiff.eval.internal.MatchResult;
import fr.inria.diverse.tracemm.semdiff.eval.internal.Util;

public class DomainSpecificTraceMatching {

	@Test
	public void testmodel_2() {
		MatchResult result = Util.matchTestmodel(2, 2, true);
		assertTrue(result.matchedWithoutErrors());
		assertTrue(result.matches());
	}

	@Test
	public void anonCompany_ExampleB_V1_V2_false_false() {
		MatchResult result = Util.matchAnonExampleB(1, 2, false, false, false,
				true);
		assertTrue(result.matchedWithoutErrors());
		assertTrue(result.matches());
	}

	@Test
	public void anonCompany_ExampleB_V1_V2_true_false() {
		MatchResult result = Util.matchAnonExampleB(1, 2, true, false, false,
				true);
		assertTrue(result.matchedWithoutErrors());
		assertFalse(result.matches());
	}

}
