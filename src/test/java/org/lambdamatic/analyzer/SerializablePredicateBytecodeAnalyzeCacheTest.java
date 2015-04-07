/**
 * 
 */
package org.lambdamatic.analyzer;

import static org.lambdamatic.testutils.JavaMethods.Object_equals;
import static org.lambdamatic.testutils.JavaMethods.TestPojo_getStringValue;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.lambdamatic.SerializablePredicate;
import org.lambdamatic.analyzer.ast.node.InfixExpression;
import org.lambdamatic.analyzer.ast.node.InfixExpression.InfixOperator;
import org.lambdamatic.analyzer.ast.node.LambdaExpression;
import org.lambdamatic.analyzer.ast.node.LocalVariable;
import org.lambdamatic.analyzer.ast.node.MethodInvocation;
import org.lambdamatic.analyzer.ast.node.StringLiteral;

import com.sample.model.TestPojo;

/**
 * @author Xavier Coulon <xcoulon@redhat.com>
 */
public class SerializablePredicateBytecodeAnalyzeCacheTest {

	@Test
	public void shouldNotAnalyzeTwice() throws NoSuchMethodException, SecurityException {
		// given
		final LambdaExpressionAnalyzer lambdaAnalyzer = LambdaExpressionAnalyzer.getInstance();
		lambdaAnalyzer.resetHitCounters();
		final LocalVariable testPojo = new LocalVariable(0, "t", TestPojo.class);
		final MethodInvocation getStringValue = new MethodInvocation(testPojo, TestPojo_getStringValue);
		// when (first call) 
		final LambdaExpression lambdaExpression1 = getLambdaExpression();
		// then 
		Assertions.assertThat(lambdaExpression1.getExpression()).isEqualTo(new InfixExpression(InfixOperator.CONDITIONAL_OR, 
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("john")),
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("jack"))
				));
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheMisses()).isEqualTo(1);
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheHits()).isEqualTo(0);
		// given
		lambdaAnalyzer.resetHitCounters();
		// when (second call)
		final LambdaExpression lambdaExpression2 = getLambdaExpression();
		// then 
		Assertions.assertThat(lambdaExpression2.getExpression()).isEqualTo(new InfixExpression(InfixOperator.CONDITIONAL_OR, 
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("john")),
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("jack"))
				));
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheMisses()).isEqualTo(0);
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheHits()).isEqualTo(1);
	}

	@Test
	public void shouldNotAnalyzeTwiceWithCapturedArguments() throws NoSuchMethodException, SecurityException {
		// given
		final LambdaExpressionAnalyzer lambdaAnalyzer = LambdaExpressionAnalyzer.getInstance();
		lambdaAnalyzer.resetHitCounters();
		final LocalVariable testPojo = new LocalVariable(0, "t", TestPojo.class);
		final MethodInvocation getStringValue = new MethodInvocation(testPojo, TestPojo_getStringValue);
		// when (first call) 
		final LambdaExpression lambdaExpression1 = getLambdaExpression("john1", "jack1");
		// then 
		Assertions.assertThat(lambdaExpression1.getExpression()).isEqualTo(new InfixExpression(InfixOperator.CONDITIONAL_OR, 
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("john1")),
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("jack1"))
				));
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheMisses()).isEqualTo(1);
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheHits()).isEqualTo(0);
		// given
		lambdaAnalyzer.resetHitCounters();
		// when (second call)
		final LambdaExpression lambdaExpression2 = getLambdaExpression("john2", "jack2");
		// then 
		Assertions.assertThat(lambdaExpression2.getExpression()).isEqualTo(new InfixExpression(InfixOperator.CONDITIONAL_OR, 
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("john2")),
				new MethodInvocation(getStringValue, Object_equals, new StringLiteral("jack2"))
				));
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheMisses()).isEqualTo(0);
		Assertions.assertThat(LambdaExpressionAnalyzer.getInstance().getCacheHits()).isEqualTo(1);
		
	}
	
	private LambdaExpression getLambdaExpression() {
		// given
		final SerializablePredicate<TestPojo> expr = ((TestPojo t) -> t.getStringValue().equals("john") || t.getStringValue().equals("jack"));
		// when
		return LambdaExpressionAnalyzer.getInstance().analyzeExpression(expr);
	}

	private LambdaExpression getLambdaExpression(final String stringField1, final String stringField2) {
		// given
		final SerializablePredicate<TestPojo> expr = ((TestPojo t) -> t.getStringValue().equals(stringField1) || t.getStringValue().equals(stringField2));
		// when
		return LambdaExpressionAnalyzer.getInstance().analyzeExpression(expr);
	}

}
