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

Le fichier d'entrée pour l'arbre ternaire doit respecter cette structure :

```
x, y, c1, c2, c3
```
Où :
- ```x``` et ```y``` sont les coordonnées du centre de division.
- ```c1``` couleur de la region Ouest.
- ```c2``` couleur de la region Nord-Est.
- ```c3``` couleur de la region Sud-Est.

#### Voici un exemple de fichier d'entrée pour la seconde variante
```
1000 
5 
600,500,R,G,J
900,400,J,B,N
800,300,G,R,B
850,350,R,J,R
540,120,B,R,J
19 
4 
400,300,G 
570,250,G 
700,10,R 
580,12,G
```
