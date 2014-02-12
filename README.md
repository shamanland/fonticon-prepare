FontIcon Prepare for Android
====

This is small app which can prepare font-based icons for Android.

It's designed to be used for [FontIcon Library][1].

Limitations
----

Currently there is implemented only [Fontastic][2] archive parser.

Assembly
----

    gradle installApp

Usage
----

1. Download your font from [Fontastic][2]
2. Assemble the app
3. Run `./build/install/prepare/bin/prepare fontastic /path/to/archive.zip`
4. Check files `icons.ttf` and `icons.xml`
5. Put `icons.ttf` to `assets` directory, `icons.xml` to `res/values/` directory

[1]: https://github.com/shamanland/fonticon
[2]: http://fontastic.me/app
