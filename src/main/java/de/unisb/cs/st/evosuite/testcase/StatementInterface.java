/**
 * Copyright (C) 2012 Gordon Fraser, Andrea Arcuri
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Public License along with
 * EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package de.unisb.cs.st.evosuite.testcase;

import java.io.PrintStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.commons.GeneratorAdapter;

import de.unisb.cs.st.evosuite.assertion.Assertion;

/**
 * @author Sebastian Steenbuck
 * 
 */
public interface StatementInterface {

	/**
	 * Check if the statement makes use of var
	 * 
	 * @param var
	 *            Variable we are checking for
	 * @return True if var is referenced
	 */
	public boolean references(VariableReference var);

	/**
	 * Replace a VariableReference with another one
	 * 
	 * @param var1
	 *            The old variable
	 * @param var2
	 *            The new variable
	 */
	public void replace(VariableReference var1, VariableReference var2);

	/**
	 * This method executes the statement under the given scope. If execution of
	 * the statement is aborted abnormally (i.e. an exception is thrown.) The
	 * exception is returned. Otherwise the return value is null.
	 * 
	 * @param scope
	 *            the scope under which the statement is executed
	 * @param out
	 * @return if an exception was thrown during execution this is the exception
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Throwable execute(Scope scope, PrintStream out)
	        throws InvocationTargetException, IllegalArgumentException,
	        IllegalAccessException, InstantiationException;

	/**
	 * Various consistency checks. This method might also return with an
	 * assertionError Functionality might depend on the status of
	 * enableAssertions in this JVM
	 * 
	 * @return
	 */
	public boolean isValid();

	/**
	 * Generate bytecode by calling method generator
	 * 
	 * @param mg
	 */
	public void getBytecode(GeneratorAdapter mg, Map<Integer, Integer> locals,
	        Throwable exception);

	/**
	 * 
	 * @return Generic type of return value
	 */
	public Type getReturnType();

	/**
	 * 
	 * @return Raw class of return value
	 */
	public Class<?> getReturnClass();

	/**
	 * Equality check
	 * 
	 * @param s
	 *            Other statement
	 * @return True if equals
	 */
	@Override
	public boolean equals(Object s);

	/**
	 * Generate hash code
	 */
	@Override
	public int hashCode();

	/**
	 * Create a string representing the statement as Java code
	 * 
	 * @return
	 */
	public String getCode();

	/**
	 * Create a string representing the statement as Java code
	 * 
	 * @return
	 */
	public String getCode(Throwable exception);

	/**
	 * @return Variable representing return value
	 */
	public VariableReference getReturnValue();

	public Set<VariableReference> getVariableReferences();

	public List<VariableReference> getUniqueVariableReferences();

	/**
	 * 
	 * @param newTestCase
	 *            the testcase in which this statement will be inserted
	 * @return
	 */
	public StatementInterface clone(TestCase newTestCase);

	/**
	 * 
	 * @param newTestCase
	 *            the testcase in which this statement will be inserted
	 * @return
	 */
	public StatementInterface copy(TestCase newTestCase, int offset);

	/**
	 * Create deep copy of statement
	 */
	public StatementInterface clone();

	/**
	 * 
	 * @param newTestCase
	 *            the testcase in which this statement will be inserted
	 * @return
	 */
	public Set<Assertion> copyAssertions(TestCase newTestCase, int offset);

	/**
	 * Check if there are assertions
	 * 
	 * @return True if there are assertions
	 */
	public boolean hasAssertions();

	/**
	 * Add a new assertion to statement
	 * 
	 * @param assertion
	 *            Assertion to be added
	 */
	public void addAssertion(Assertion assertion);

	/**
	 * Sets the set of assertions to statement
	 * 
	 * @param assertion
	 *            Assertions to be added
	 */
	public void setAssertions(Set<Assertion> assertions);

	/**
	 * Get Java code representation of assertions
	 * 
	 * @return String representing all assertions attached to this statement
	 */
	public String getAssertionCode();

	/**
	 * Delete all assertions attached to this statement
	 */
	public void removeAssertions();

	/**
	 * Delete assertion attached to this statement
	 */
	public void removeAssertion(Assertion assertion);

	/**
	 * Return list of assertions
	 */
	public Set<Assertion> getAssertions();

	public Set<Class<?>> getDeclaredExceptions();

	public int getPosition();

	/**
	 * Allows the comparing of Statements between TestCases. I.e. this is a more
	 * semantic comparison than the one done by equals. E.g. two Variable are
	 * equal if they are at the same position and they reference to objects of
	 * the same type.
	 * 
	 * @param s
	 * @return
	 */
	public boolean same(StatementInterface s);

	/**
	 * Tests if the throwable defined by t is declared to be thrown by the
	 * underlying type. Obviously this can only return true for methods and
	 * constructors.
	 * 
	 * @param t
	 * @return
	 */
	public boolean isDeclaredException(Throwable t);

	public boolean mutate(TestCase test, AbstractTestFactory factory);

	public void setRetval(VariableReference newRetVal);

	/**
	 * Returns the accessibleObject which is used to generate this kind of
	 * statement E.g. the Field of a FieldStatement, the Method of a
	 * MethodStatement and so on MAY return NULL (for example for
	 * NullStatements)
	 * 
	 * @return
	 */
	public AccessibleObject getAccessibleObject();

	/**
	 * Returns true if this statement should be handled as an
	 * AssignmentStatement. This method was added to allow the wrapping of
	 * AssignmentStatements (in which case "a instanceof AssignmentStatement" is
	 * no longer working)
	 * 
	 * @return
	 */
	public boolean isAssignmentStatement();

	/**
	 * Class instances are bound to a class loader - if we want to reexecute a
	 * test on a different classloader we need to be able to change the class of
	 * the reflection object
	 * 
	 * @param loader
	 */
	public void changeClassLoader(ClassLoader loader);
}