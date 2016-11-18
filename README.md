**PLEASE NOTE: This tool is currently under active development and not ready for use. Please check back again later or watch for updates.**

# mutcl

A mutation testing tool written in Clojure for Clojure (Leiningen) projects.

Mutation testing provides a litmus for the quality of a project's tests. That is, mutation testing tests tests for sensitivity to bugs. Assuming a project passes its test suite, mutcl introduces random bugs to the source, runs the test suite, and looks for test failures. A test suite of adequate coverage should fail as bugs are added.

Mutcl introduces random bugs to Clojure programs by exploiting the homoiconeity that defines Lisp syntaxes. The program is traversed as a set of trees, represented as recursively defined nested lists. This stocastic algorithm applies a substitution to a random element of the program (i.e., changes the expression (+ 2 2) to (- 2 2)).

The design of the substituion function presented a significant challenge in the context of a dynamically typed language like Clojure. An ideal substitution function avoids costly semantically or syntactically invalid changes that producetrivial runtime errors by substituting symbols based on type signiture. However, type behavior in a dynamically typed language, by definition, is only known at runtime. Thus, the current, tentative solution to this problem involves hardcoding a map of valid substitutions as categories of similiar type behavior. This approach, while reliable, biases the introduced substituions to a small subset of Clojure's core, ultimately offering only partial insight into the test's coverage of the codebase. For instance, user defined function references will not mutated.

## License

Copyright © 2016 David Shaked

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
