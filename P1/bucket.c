#include <stdlib.h>
#include <stdio.h>
/*
#include <pthread.h>
#include <unistd.h>

struct canasta{
    int *entrada;
    int limSup;
    int limInf;
}
*/

int * generarLista(){
    static int lista[3500];
    time_t t;

    /* Inicializa seed de rand() */
    srand((unsigned) time(&t));
    for(int i = 0; i < 3500; i++){
        lista[i] = rand() % 999; // Random de 0 a 999
    }
    
    return lista;
}
/*
int * ordenar(int *listaAcotada){
    
}

void dispararThreads(int numThreads){

}
*/

int main(int argc, char const *argv[]){
    int *lista = generarLista();
    if(argv[1] > 0){
        int tamArr = 3500 / atoi(argv[1]); 
        printf("tamArr: %d\n", tamArr);
        for(int i = 0; i < 3500; i++){
            printf("%d, ", lista[i]);
        }
    }
    else{
        printf("ERROR: Ingresa un numero valido de canastas");
        exit(0);
    }
}