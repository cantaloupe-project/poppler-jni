# poppler-jni

JNI binding to the C++ interface of the
[Poppler](https://poppler.freedesktop.org) PDF library.

Not all Poppler API functions are implemented--mostly only what would be
needed to support an Image I/O plugin (that has not yet been developed). Pull
requests that expand the API coverage are welcome.

# Development

## Requirements

* Java 9+
* libpoppler
* gradle (for the Java stuff)
* cmake & make (to build the shared library)

## Making changes to native code

After adding, removing, or changing the signatures of any `native` Java
methods:

1. Invoke `generate_headers.sh`. This will regenerate the `.h` files in
   `src/main/cpp`.
2. For each of the header file function signatures that changed, update the
   corresponding function in the corresponding `.cpp` file.
3. Invoke `cmake .; make` to rebuild the shared library.
