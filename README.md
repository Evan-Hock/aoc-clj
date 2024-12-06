# aoc

This thing acts as a framework around which you can structure your [Advent of Code](https://adventofcode.com) solutions.

I've also put my aoc solutions here.

The main piece of action resides in the `puzzle` function, which will get your puzzle input
and run your code for you.

## Usage

In order to use this library, you will need to log in to [Advent of Code](https://adventofcode.com) and get your
user session ID. You can find this by getting your puzzle input and analyzing the network traffic (use your browser's inspector); the key will
be present in the cookie header. It's the long string after the "session=" part.

Once you have that, set the env var `AOC_SESSION`:

```bash
export AOC_SESSION=<your-session-id>
```

or, with Powershell

```ps1
$Env:AOC_SESSION = <your-session-id>
```

or, with the command prompt

```bat
set AOC_SESSION=<your-session-id>
```

Additionally, you should also set your user agent by setting the AOC_USER_AGENT environment variable in the same way as above.

## License

Copyright © 2024 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
