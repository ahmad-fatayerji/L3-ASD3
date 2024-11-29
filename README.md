## FATAYERJI Ahmad et HALGAND Kyllian - MonBoTablo

### Compilation

Pour compiler le programme, utiliser la commande suivante depuis le répertoire du projet :

```shell
javac -source 1.7 -target 1.7 -d FatayerjiHalgand/bin FatayerjiHalgand/src/*.java
```
### Execution

#### Pour Quadtree
```
java -classpath FatayerjiHalgand/bin MonBoTablo 1 FatayerjiHalgand/data/input1.txt FatayerjiHalgand/output
```

#### Pour Ternarytree
```
java -classpath FatayerjiHalgand/bin MonBoTablo 2 FatayerjiHalgand/data/input2.txt FatayerjiHalgand/output
```

## Variante Ternarytree

### Explication de la variante

Pour l'arbre ternaire, nous avons choisi de combiner la région Nord-Ouest et la région Sud-Ouest en une seule région appelée Ouest. Ainsi, l’arbre est divisé en trois régions, dont l’une est plus étendue que les deux autres.

L'arbre ternaire divise une région rectangulaire en trois sous-régions en utilisant un point de division donné (x, y). Ces sous-régions sont définies comme suit :

1. __Région Ouest (O)__ : Cette région est colorée avec la couleur ```c1```.
2. __Région Nord-Est (NE)__ : Cette région est colorée avec la couleur ```c2```.
3. __Région Sud-Est (SE)__ : Cette région est colorée avec la couleur ```c3```.

### Comment fonctionne l'arbre ternaire pour les divisions ?

Un arbre ternaire représente une région rectangulaire qui peut être divisée en trois sous-régions lorsque nécessaire. Cette division est contrôlée par un point de division ```(dx, dy)``` fourni en entrée. Voici une explication étape par étape de la logique de division.

1. Etat initial
- Chaque nœud dans l'arbre ternaire représente une région rectangulaire définie par :
    - __Point inférieur gauche (```bottomLeft```) :__ ```(x0, y0)```
    - __Point supérieur gauche (```topRight```) :__ ```(x1, y1)```
- Si le nœud est une __feuille__ (sans enfants), il représente une région qui peut être divisée.
- Le __point de division__ ```(dx, dy)``` est l'élément clé qui déclenche la division.

2. Processus de division
Lorsqu’un nœud feuille est divisé, il divise la région rectangulaire en trois __sous-régions__ :
    1.  __Région Ouest :__ La partie située à gauche du point de division.
    2.  __Région Nord-Est :__ La partie située au-dessus et à droite du point de division.
    3.  __Région Sud-Est :__ La partie située en dessous et à droite du point de division.
- Chaque sous-région est associée à une couleur (```c1```, ```c2```, ```c3```) fournie en entrée avec le point de division.

3. Définition des sous-régions

Soit :
- __La région d’origine :__ ```(x0, y0)``` à ```(x1, y1)```
- __Le point de division :__ ```(dx, dy)```
- __Les couleurs :__ ```c1```, ```c2```, ```c3```

Les sous-régions sont définies comme suit :

1. Région Ouest:
- Coordonnées :
    - Inférieur gauche : ```(x0, y0)```
    - Supérieur droit :  ```(dx, y1)```
    - Couleur : ```c1```
Contient tous les points où ```x <= dx```.
2. Région Nord-Est:
- Coordonnées :
    - Inférieur gauche :```(dx, dy)```
    - Supérieur droit : ```(x1, y1)```
    - Couleur : ```c2```
Contient tous les points où ```x > dx et y >= dy```.
3. Région Sud-Est:
- Coordonnées :
    - Inférieur gauche :```(dx, y0)```
    - Supérieur droit : ```(x1, dy)```
    - Couleur : ```c3```
Contient tous les points où ```x > dx et y < dy```.

4. Règles de division
- Un nœud est __remplacé par trois enfants__, chacun représentant une des trois sous-régions.
- Le point de division ```(dx, dy)``` doit se situer strictement à l’intérieur de la région (pas sur une bordure). Si le point de division est invalide (par exemple, en dehors de la région), la division est ignorée.

### Format du fichier d'entrée pour la variante 2

Le fichier d'entrée pour l'arbre ternaire respecte la meme structure que la ```variante 1``` __sauf pour les points__, voici un exemple :

```
x, y, c1, c2, c3
```
Où :
- ```x``` et ```y``` sont les coordonnées du centre de division.
- ```c1``` couleur de la region __Ouest__.
- ```c2``` couleur de la region __Nord-Est__.
- ```c3``` couleur de la region __Sud-Est__.

#### Voici un exemple de fichier d'entrée pour la seconde variante
```
1000                //ligne 1 : pour l'image initiale, le côté n (en nombre de pixels). Ici n = 1000.
5                   //ligne 2 : le nombre m >= 0 de centres fournis. Ici m = 5.
600,500,R,G,J       //ligne 3 : le 1er centre.
900,400,J,B,N       //ligne 4 : le 2ème centre.
800,300,G,R,B       //etc
850,350,R,J,R       
540,120,B,R,J       //le m-ème centre.
19                  //l'épaisseur e >= 1 (valeur impaire) du trait. Ici e = 19.
4                   //nombre k >= 0 de paires de recoloriage fournies ci-dessous, pour recoloriages successifs.
400,300,G           //première paire, sous la forme x; y; couleur.
570,250,G           //deuxième paire.
700,10,R            //etc.
580,12,G            //dernière paire.
```