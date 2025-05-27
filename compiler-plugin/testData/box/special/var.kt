public var a
  get() = 2
  set(value) {} // getter and setter follows property

public var b
  get() = 2
  internal set(value) {} // setter can be narrowed

fun box() = "OK"