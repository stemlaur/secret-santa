# secret santa

## What is it

A program for secret santa to assign present givers and receivers using a list of couples.

* the selection must be random
* a participant cannot give its present to the participant who gave him its present
* a participant cannot receive or give a present from its couple
* a participant cannot give a present to itself ^^

## how to run

Update the file `couples.txt` with your couples list. Couples are separated with '/'.
Mention people that are not in a couple in separate lines.

For example:

```text
Alice/Bob
Charles/Doc
Emile
Francois/Georges
```

Alice and Bob are in couple.
Charles and Doc are in couple.
Emile is not in couple.
Francois and Georges are in couple.

Then simply run the program


```mermaid
  graph TD;
      A-->B;
      A-->C;
      B-->D;
      C-->D;
```