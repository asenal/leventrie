# leventrie

Trie is a efficient data structure,  levenshtein distance is a
well-defined metric for string similarity.
This toy code support quick search in a trie for strings within a
certain levenschtein distance to a given query string.

## Contact

qiulin.work@gmail.com

## Installation

Download from https://github.com/asenal/leventrie

## Usage

Input a library file and convert into a trie, for each word from *stdin*, do a quick search agaist the trie, output terms within given levenschtein distance.


## Options

 -d : a non-negative integer, the maxmium distance allowed

 library.txt is the first arg, a library upon which a trie is build, the name doesn't have to be library.txt

## Examples

    $ cat query.txt | java -jar leventrie-0.1.0-standalone.jar library.txt -d 3

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
