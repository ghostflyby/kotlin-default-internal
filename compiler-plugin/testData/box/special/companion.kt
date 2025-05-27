private class C {
  companion object {
    public operator fun invoke() = C()
  } // default public visibility for companion object
}

fun box() = "OK"