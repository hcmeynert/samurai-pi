# samurai-pi

A Java console Program to calculate up to 1,000,000 decimal places of Pi using a quickly convergent series for Pi derived from an Edo-Japan series. The series is the square root series by the samurai Takebe Katahiro (1664-1739) who was an advisor of the shogun. He deduced, within this, the coefficients of the series by conjecture, i. e. by improper induction. He calculated, say, the first ten or fifteen coefficients, and from these, he inferred a general law for all of the summands. This method he called "tetsujutsu" "combining the series".

(Information taken from book "Japanese Mathematics in the Edo Period" by Annick Horiuchi)

The complete proof for the validity of the series used in the program is given in the added PDF.

So the emphasis in the project is on the ability of the user of the program to guarantee that the digits calculated be correct.

No external libraries are needed, just the standard JDK.

$ javac SamuraiPi.java

$ java SamuraiPi
