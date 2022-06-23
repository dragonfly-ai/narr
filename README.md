# narr
pronounced: ˈnär, as in gnarly, narr stands for Native Array.

This library provides Scala.js cross projects with an abstraction over features common to js.Array and scala.Array and also includes polyfills for features like tabulate and fill.

Using narr.ARRAY instead of js.Array and scala.Array ensures that a project will always use the native Array type of the platform it compiles to.
