#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <string.h>
#include <unistd.h>
#define MAX 3500
#define HOLGURA 35

typedef struct intervalo{
    int inf;
    int sup;
} Intervalo;

typedef struct canasta{
    Intervalo inter;
    int *entrada;
    int size;
} Canasta;

Canasta crearCanasta(Intervalo inter, int size){
    Canasta *basket = NULL;
    basket = (Canasta*)malloc(sizeof(Canasta));
    basket->entrada = malloc(size * sizeof(int));
    memset(basket->entrada, -1, size);
    basket->inter = inter;
    return *basket;
}

/* Funcion de comparacion para qsort()*/
int comparaInt(const void* prim, const void* seg){
    int x = *((int*)prim), y =  *((int*)seg);
    if (x == y)
        return 0;
    else if (x < y)
        return -1;
    else
        return 1;
}

/* Genera una lista de tamaño MAX de numeros random */
int * generarLista(){
    static int lista[MAX];
    time_t t;
    /* Inicializa seed de rand() */
    srand((unsigned) time(&t));
    printf("\nLista completa desordenada\n");
    for(int i = 0; i < MAX; i++){
        lista[i] = rand() % MAX; // Random de 0 a 999
        printf("%d. ", lista[i]);
    }
    return lista;
}

/* Devuelve el numero de canasta que le corresponde a cada elemento de la lista */
int determinarCanasta(Intervalo * inter, int numhilos, int num){
    for(int i = 0; i < numhilos; i++){
        if(num >= inter[i].inf && num < inter[i].sup)
            return i;
    }
}

/* Define los intervalos de acuerdo al numero de canastas (hilos) requeridos */
Intervalo * crearIntervalo(int tamArr, int numhilos){
    Intervalo *aux = malloc(numhilos*sizeof(Intervalo));
    aux[0].inf = 0;
    aux[0].sup = tamArr + HOLGURA;
    for (int i = 1; i < numhilos; i++){
        aux[i].inf = tamArr*i + HOLGURA;
        aux[i].sup = tamArr*(i+1) + HOLGURA;
        /* Imprime los limites de intervalo 
        printf("\nInf: %d\n", aux[i].inf);
        printf("Sup: %d\n", aux[i].sup);
        */
    }
    return aux;
}

/* Devuelve el array acotado para evitar direcciones corruptas*/
int * arrayJusto(int * in, int size){
    int acum;
    int * new = malloc(size*sizeof(int));
    for(int i = 0; i < size; i++){
        if(in[i] != -1)
            acum++;
    }
    for(int i = 0; i < acum; i++){
        new[i] = in[i];
    }
    return new;
}

/* Funcion de ordenar ejecutado por cada hilo */
void * ordenar(void * arg){ // Canasta * basket, int size
    Canasta *basket = (Canasta *)arg;
    qsort(basket->entrada, basket->size, sizeof(int), &comparaInt);
}


int main(int argc, char const *argv[]){
    int *lista = generarLista();
    int salida[MAX];

    if(argv[1] > 0){
        int tamArr = MAX / atoi(argv[1]); // Tamaño maximo del arreglo
        Intervalo *inter = crearIntervalo(tamArr, atoi(argv[1])); // Arreglo de intervalos
        Canasta *basket = malloc(atoi(argv[1]) * sizeof(Canasta)); // Arreglo de canastas

        int numcan = 0;
        int acum[atoi(argv[1])]; // Arreglo de acumuladores para controlar entradas a canasta

        for (int i = 0; i < atoi(argv[1]); i++){
            basket[i] = crearCanasta(inter[i], tamArr+HOLGURA);
            acum[i] = 0;
        }
        
        for(int i = 0; i < MAX; i++){
            numcan = determinarCanasta(inter, atoi(argv[1]), lista[i]);
            basket[numcan].entrada[acum[numcan]] = lista[i];
            //printf("%d. ", numcan);
            acum[numcan]++;
        }
              
        for(int i = 0; i < atoi(argv[1]); i++){
            basket[i].entrada = arrayJusto(basket[i].entrada, acum[i]);
            basket[i].size = acum[i];
        }
        
        /* Muestra los elementos por canasta de forma desordenada
        printf("\nDesordenados.\n");
        for(int i = 0; i < atoi(argv[1]); i++){
            printf("\nCanasta %d\n", i);
            for(int j = 0; j < basket[i].size; j++){
                printf("%d ", basket[i].entrada[j]);
            }
        }
        */
        
        /* Creacion de hilos */
        pthread_t hilo[atoi(argv[1])];
        for(int i = 0; i < atoi(argv[1]); i++){
            pthread_create(&hilo[i], NULL, ordenar, &basket[i]);
            pthread_join(hilo[i], NULL);
        }

        /* Muestra los elementos por canasta de forma ordenada 
        printf("\n\nOrdenados.\n");
        for(int i = 0; i < atoi(argv[1]); i++){
            printf("\nCanasta %d\n", i);
            for(int j = 0; j < basket[i].size; j++){
                printf("%d ", basket[i].entrada[j]);
            }
        }
        */

        printf("\n\nLista completa ordenada\n");
        // Reconstruccion de arreglo ordenado 
        for(int i = 0; i < atoi(argv[1]); i++){
            for(int j = 0; j < basket[i].size; j++){
                printf("%d. ", basket[i].entrada[j]);
            }
        }
    }
    else{
        printf("ERROR: Ingresa un numero valido de canastas");
        exit(0);
    }
}