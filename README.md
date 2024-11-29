## FATAYERJI Ahmad et HALGAND Kyllian - Mon Bo Tablo

### Compilation

Pour compiler le programme, utiliser la commande suivante depuis le répertoire du projet :

```shell
javac -source 1.7 -target 1.7 -d FatayerjiHalgand/bin FatayerjiHalgand/src/*.java
```
### Execution

#### Pour Quad Tree
```
java -classpath FatayerjiHalgand/bin MonBoTablo 1 FatayerjiHalgand/data/input1.txt FatayerjiHalgand/output
```

#### Pour Ternary Tree
```
java -classpath FatayerjiHalgand/bin MonBoTablo 2 FatayerjiHalgand/data/input2.txt FatayerjiHalgand/output
```

## Variante Ternary tree

### Explication de la variante

Pour l'arbre ternaire, nous avons choisi de combiner la région Nord-Ouest et la région Sud-Ouest en une seule région appelée Ouest. Ainsi, l’arbre est divisé en trois régions, dont l’une est plus étendue que les deux autres.

### Format du fichier d'entrée pour la variante 2

