  import java.util.List;        // for expression evaluation
  import java.util.ArrayList;    // track variables

  /**
   *    The program makes use of stacks to create calculation functions
   *    and find the expected outcome. The operator and operand stack are used hand
   *    in hand to read the user expression. By holding values between the two
   *    separate stacks, the program ensures that the entered expression is
   *    read and held in the correct order. The identifier class is used to
   *    store values into variables. It finds the associated value based on
   *    the name of a variable, and pass that value into the evaluateExpression
   *    method, so no edits were necessary to the stack alg.
   *    @author       Mayank Singamreddy
   *    @since        February 12, 2018
   */
  public class SimpleCalc {

      private ExprUtils utils;    // expression utilities

      private ArrayStack<Double> valueStack;        // value stack
      private ArrayStack<String> operatorStack;    // operator stack

      private ArrayList<Identifier> varList;

      // constructor
      public SimpleCalc() {
          utils = new ExprUtils();
          valueStack = new ArrayStack<Double>();
          operatorStack = new ArrayStack<String>();
          varList = new ArrayList<Identifier>();
      }

      public static void main(String[] args) {
          SimpleCalc simpC = new SimpleCalc();
          simpC.run();
      }

      public void run() {

          runCalc();
          System.out.println("\nThanks for using SimpleCalc! Goodbye.\n");
      }

      /**
       *    Prompt the user for expressions, replace variables with values,
       *    run the expression evaluator, and display the answer.
       */
      public void runCalc() {
      System.out.println("\nWelcome to SimpleCalc!!!");
      // initialize e and pi, with their mathematical values
  		varList.add(new Identifier("e", Math.E));
          varList.add(new Identifier("pi", Math.PI));

          boolean quit = false;
          boolean isSetVal = false;

          while(!quit)
          {
              String expression = Prompt.getString("");

              List<String> expr = utils.tokenizeExpression(expression);

              // if an equal sign is detected
              // the expression is a setting a variable to a value
              for (int i = 0; i < expr.size(); i++) {
                  if (expr.get(i).charAt(0) == '=')
                      isSetVal = true;
              }

              if (expression.equalsIgnoreCase("h"))
                  printHelp();
              // go through arraylist and print names/values (case "L")
              else if (expression.equals("l")) {
  				System.out.println("Variables: ");
                  for (int a = 0; a < varList.size(); a++)
                      System.out.printf("   %-20s = %15.3f\n",
                            varList.get(a).getName(), varList.get(a).getValue());
              }
              else if (expression.equalsIgnoreCase("q"))
                  quit = true; //exit program
              else if (isSetVal) {
                  boolean invalid = false;

  				// check for if the var is reserved
  				// if reserved, place invalidity mark
                  if (expr.get(0).equalsIgnoreCase("h") ||
                                expr.get(0).equalsIgnoreCase("q")
                      || expr.get(0).equalsIgnoreCase("e") ||
                                expr.get(0).equalsIgnoreCase("pi")
                      || expr.get(0).equalsIgnoreCase("l"))
                          invalid = true;

  				// check for if name has digits
  				// if digits exist, mark with invalidity
                  for (int a = 0; a < expr.get(0).length(); a++) {
                      if (Character.isDigit(expr.get(0).charAt(a)))
                          invalid = true;
                  }

  				// check for if name already exists
  				// if already used, remove old val to get rid of duplicate
                  for (int a = 2; a < varList.size(); a++) {
  					if (varList.get(a).getName().equals(expr.get(0))) {
  						varList.remove(a);
  						a = varList.size();
  					}
                  }

          // create new list with expression part of the input
  				// remove the tokens which are var name and "="
                  List<String> exprPart = utils.tokenizeExpression(expression);
                  exprPart.remove(0);
                  exprPart.remove(0);

  				// if name is valid
  				// loop through and replace with values
  				// then evaluate and create identifier
  				// or else, print out invalid var used
                  if (!invalid) {
                      for (int a = 0; a < exprPart.size(); a++) {
                          if (isVariable(exprPart.get(a)))
                              for (int b = 0; b < varList.size(); b++) {
                                  if (varList.get(b).getName().equals(exprPart.get(a)))
                                      exprPart.set(a, "" + varList.get(b).getValue());
                              }
                      }
                      varList.add(new Identifier(expr.get(0), evaluateExpression(exprPart)));
                      System.out.printf(expr.get(0) + " = " + evaluateExpression(exprPart) + "\n");
                  }
                  else
                      System.out.println("Invalid variable name used.");

              }
              else {
  				// loop through and replace vars with
  				// the associated values
  				// if a variable is not initialized, use 0 as the value
                  for (int g = 0; g < expr.size(); g++) {
                      if (isVariable(expr.get(g))) {
                          boolean found = false;
                          for (int b = 0; b < varList.size(); b++) {
                              if (varList.get(b).getName().equals(expr.get(g))) {
                                  expr.set(g, "" + varList.get(b).getValue());
                                  found = true;
                              }
                          }
                          if (!found)
                              expr.set(g, "0");
                      }
                  }
                  System.out.printf("%.3f\n", evaluateExpression(expr));
              }

              isSetVal = false;
          }
      }

     /**
      * Method check for if a token is a variable, based on if it begins
      * with a letter
      *
      * @param str     token
      * @return        boolean if variable or not
      */
      public boolean isVariable(String str) {
          if (Character.isLetter(str.charAt(0)))
              return true;
          return false;
      }

      /**  Method for printing help/user instructions */
      public void printHelp() {
          System.out.println("Help:");
          System.out.println("  h - this message\n  q - quit\n");
          System.out.println("Expressions can contain:");
          System.out.println("  integers or decimal numbers");
          System.out.println("  arithmetic operators +, -, *, /, %, ^");
          System.out.println("  parentheses '(' and ')'");
      }

      public double evaluateExpression(List<String> tokens) {
          double value = 0;

          while (tokens.size() > 0) {
              String temp = tokens.remove(0);
              if (Character.isDigit(temp.charAt(0)) || temp.charAt(0) == '.' || (temp.length() > 1 && temp.charAt(0) == '-'))
                  valueStack.push(Double.parseDouble(temp));
              else if (temp.equals("("))
                  operatorStack.push(temp);
              else if (temp.equals(")"))
              {
                  while (!operatorStack.peek().equals("("))
                      valueStack.push(evaluate(valueStack.pop(), valueStack.pop(), operatorStack.pop().charAt(0)));

                  operatorStack.pop();
              }
              else if (utils.isOperator(temp.charAt(0)))
              {
                  while (!operatorStack.isEmpty() && hasPrecedence(temp, operatorStack.peek()))
                      valueStack.push(evaluate(valueStack.pop(), valueStack.pop(), operatorStack.pop().charAt(0)));

                  operatorStack.push(temp);
              }
          }
          while (!operatorStack.isEmpty())
              valueStack.push(evaluate(valueStack.pop(), valueStack.pop(), operatorStack.pop().charAt(0)));

          value = valueStack.peek();

          return value;
      }

     /**
      * The method takes in the two values, applies the operand, and returns
      * the expected value of the expression.
      *
      * @param v1        the first operand
      * @param v2        the second operand
      * @param op        the operator to apply
      * @return        the evaluated result
      */
      public double evaluate(double v1, double v2, char op)
      {
          switch (op)
          {
              case '+':
                  return v1 + v2;
              case '*':
                  return v1 * v2;
              case '-':
                  return v2 - v1;
              case '/':
                  return v2 / v1;
              case '%':
                  return v2 % v1;
              case '^':
                  return Math.pow(v2, v1);

          }
          // return -1 for invalid operators
          return -1;
      }

      /**
       *    Finds the precendce of operators
       *    @param op1        operator 1
       *    @param op2        operator 2
       *    @return            true if op2 has higher or same precedence as op1; false otherwise
       *    Algorithm:
       *        if op1 is exponent, then false
       *        if op2 is either left or right parenthesis, then false
       *        if op1 is multiplication or division or modulus and
       *                op2 is addition or subtraction, then false
       *        otherwise true
       */
      private boolean hasPrecedence(String op1, String op2) {
          if (op1.equals("^")) return false;
          if (op2.equals("(") || op2.equals(")")) return false;
          if ((op1.equals("*") || op1.equals("/") || op1.equals("%"))
                  && (op2.equals("+") || op2.equals("-")))
              return false;
          return true;
      }

  }