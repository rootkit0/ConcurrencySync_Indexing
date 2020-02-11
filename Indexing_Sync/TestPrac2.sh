#!/bin/bash

echo "Vaciar directorios Output ejemplo"
rm output/example1/*
mkdir output/example1/
rm output/example2/*
mkdir output/example2/
rm output/example3/*
mkdir output/example3/
rm output/quijote/*
mkdir output/quijote/

echo "Ejecutar Indexado"
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Indexing test/example1.txt 8 10 output/example1
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Indexing test/example2.txt 8 10 output/example2
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Indexing test/example3.txt 8 10 output/example3
echo "Indexando Quijote Secuencial"
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Indexing test/pg2000.txt 1 10 output/quijote/
echo "Indexando Quijote Concurrente"
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Indexing test/pg2000.txt 8 10 output/quijote/
echo "Validar Indices generados"
cat output/example1/IndexFile* | cut -f2 | tr ',' ' '  | wc -w
cat output/example2/IndexFile* | cut -f2 | tr ',' ' '  | wc -w
cat output/example3/IndexFile* | cut -f2 | tr ',' ' '  | wc -w
cat output/quijote/IndexFile* | cut -f2 | tr ',' ' '  | wc -w

echo "Query Quijote Secuencial"
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Query "En un lugar de la Mancha" output/quijote/ test/pg2000.txt 1 10
echo "Query Quijote Concurrente"
time java -cp out/artifacts/Indexing_jar/Indexing.jar eps.scp.Query "En un lugar de la Mancha" output/quijote/ test/pg2000.txt 8 10
