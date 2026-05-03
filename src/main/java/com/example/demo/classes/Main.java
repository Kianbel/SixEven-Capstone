package com.example.demo.classes;

public class Main {
    public static void main(String[] args) {
        //Make a function and pass the function name
        Function sum = new Function("sum");
        //The standard procedure is to declare the variables and then add them to the function later
        //also, you can get the variable from the declaration object so making a variable is standard
        //before adding it to the function
        Declaration d1 = new Declaration("n");
        Declaration d2 = new Declaration("i");
        Declaration d3 = new Declaration("sum");
        Declaration d4 = new Declaration("j");
        Declaration d5 = new Declaration("k");
        //we add one of the declaration as parameter, specifically n
        sum.addParameter(d1);
        //we add the sum variable to the function's body
        sum.addStatement(d3);
        //we add initialize the sum variable to 0
        sum.addStatement(new Initialization(d3.getVariable(), new Value<Integer>(0)));
//        sum.addStatement(new Initialization(d1.getVariable(), new Value<Integer>(10))); //this is for testing
        //we declare the i variable in the function's body
        sum.addStatement(d2);
        //we make a for loop, first parameter is a singlestatement, in this case we passed an initialization
        //which is a subclass of singlestatement, we initialize the variable i to 0
        //the second parameter is a Bound object. there are two types, an upper and lower bound
        //and those two also have another two types, left and right
        //for now we can only make a rightupperbound for minimalism
        //it basically means that our iterator is on the left side and the limit where we stop is on the right side
        //example : i <= n -> iterator is on the left side and n is on the right side
        //we restrict this for now because n >= i, a leftupperbound, achieves same results but we have to code more
        //lowerbound is something like this i >= 0 where the iterator is decreasing
        //we don't allow this yet because we have to code a lot
        //another parameter of the Bound is an isEqual boolean, which is basically if we want it to be
        // simply < or <= (less or less than or equal)
        //The last parameter of the loop is an assignment for example +=, *=, /=
        //there are two types, increasing and decreasing
        //increasing are those that increase the value and decreasing are those that decrease the value
        //for now we only allow increasing to make it simpler and for now we only allow +=
        //we can't do i++ as well because it's a bit complicated and let's stick with i += 1
        ForLoop f1 = new ForLoop(new Initialization(d2.getVariable(), new Value<Integer>(0)), new RightUpperBound(d2.getVariable(), d1.getVariable(), false), new AdditiveAssignment(d2.getVariable(), new Value<Integer>(3)));
        //we add variable j to the first for loop's body
        f1.addStatement(d4);
        //we make another for loop with the iterator j
        //this time, instead of simply making the bound a variable, we can make it an expression using a binary operation
        //in this case we make the upper limit j < i+1
        ForLoop f2 = new ForLoop(new Initialization(d4.getVariable(), new Value<Integer>(0)), new RightUpperBound(d4.getVariable(), new Binary(new Operator("+"),d2.getVariable(), new Value<Integer>(1)), false), new AdditiveAssignment(d4.getVariable(), new Value<Integer>(1)));
        //we make another for loop with the iterator k
        ForLoop f3 = new ForLoop(new Initialization(d5.getVariable(), new Value<Integer>(0)), new RightUpperBound(d5.getVariable(), d4.getVariable(), false), new AdditiveAssignment(d5.getVariable(), new Value<Integer>(1)));
        //we put the second for loop inside the first for loop
        f1.addStatement(f2);
        //we add the first for loop inside the sum function
        sum.addStatement(f1);
        //we declare variable k in the second for loop
        f2.addStatement(d5);
        //we add the third for loop inside the second for loop
        f2.addStatement(f3);
        //we make simple statement sum++ by making a Unary Operation. isPrefix false because we want it to be postfix
        f3.addStatement(new SingleLineStatement(new Unary(new Operator("++"), d3.getVariable(), false)));
        //we can print the function
        System.out.println(sum);
        //then we can get the run time
        System.out.println(sum.getRuntime());
    }
}