# generalized sparse coding neural gas

Scala implementation of generalized sparse coding neural gas algorithms based on
"Sparse Coding Neural Gas: Learning of Overcomplete Data Representations"
http://www.inb.uni-luebeck.de/publications/pdfs/LaBaMa09.pdf

The neighbourhoud radius in this implementation is depend on the residual error.
The mechanism to determine the neighbourhoud radius is calculated like the calculation of
the neigbourhoud in parameter less self organizing map.
The description of the improved parameter less som is here:
https://www.researchgate.net/publication/220204486_Improved_PLSOM_algorithm

To install:

1. git clone https://github.com/jonysugianto/gscng

2. download https://github.com/jonysugianto/gscng/breastcancerimages put into(for example: /tmp)

3. a) cd gscng
   b) mvn clean compile assembly:single

4. java -classpath generalized-sparse-coding-neural-gas-1.0-SNAPSHOT-jar-with-dependencies.jar js.example.GScNgRunner /tmp/breastcancerimages