/** @externs */

// cljsjs/react-dom 18.3.1-1 does not declare flushSync. Reagent 2 uses it
// for reactive updates; preserve the public ReactDOM property in advanced builds.
ReactDOM.flushSync = function(callback) {};
