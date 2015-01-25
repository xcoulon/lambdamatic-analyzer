package org.lambdamatic.analyzer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.lambdamatic.FilterExpression;
import org.lambdamatic.LambdaExpression;
import org.lambdamatic.analyzer.ast.node.CapturedArgument;
import org.lambdamatic.analyzer.ast.node.Expression;
import org.lambdamatic.analyzer.ast.node.InfixExpression;
import org.lambdamatic.analyzer.ast.node.InfixExpression.InfixOperator;
import org.lambdamatic.analyzer.ast.node.LocalVariable;
import org.lambdamatic.analyzer.ast.node.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sample.model.TestPojo;

/**
 * <p>
 * Running test in an isolated class to simplify the bytecode reading.
 * </p>
 * 
 * @author Xavier Coulon <xcoulon@redhat.com>
 *
 */
public class IsolatedLambdaBytecodeAnalyzerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(IsolatedLambdaBytecodeAnalyzerTest.class);

	private final LambdaExpressionAnalyzer analyzer = new LambdaExpressionAnalyzer();

	@Test
	public void shouldParseExpression() throws IOException {
		// given
		final String[] values = new String[]{new String(), new String()};
		final FilterExpression<TestPojo> expression = (TestPojo t) -> t.matches(values);
		// when
		final LambdaExpression resultExpression = analyzer.analyzeLambdaExpression(expression);
		// then
		LOGGER.info("Number of InfixExpressions used during the process: {}",
				(new InfixExpression(InfixOperator.CONDITIONAL_AND).getId() - 1));
		final LocalVariable testPojo = new LocalVariable("t", TestPojo.class);
		final Expression expected = new MethodInvocation(testPojo, "matches", Boolean.class, new CapturedArgument(values));
		// verification
		assertThat(resultExpression.getExpression()).isEqualTo(expected);
	}
}

