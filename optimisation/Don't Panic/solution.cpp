#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;
int main(){
    int nbFloors,width,nbRounds,exitFloor,exitPos,nbTotalClones,nbAdditionalElevators,nbElevators,elevators[nbElevators+1];
    
    cin >> nbFloors >> width >> nbRounds >> exitFloor >> exitPos >> nbTotalClones >> nbAdditionalElevators >> nbElevators; cin.ignore();
    for (int i = 0; i < nbElevators; i++) {
        int elevatorFloor,elevatorPos; 
        cin >> elevatorFloor >> elevatorPos; cin.ignore();
        elevators[elevatorFloor] = elevatorPos;
    }
    elevators[exitFloor] = exitPos;
    while (1) {
        int cloneFloor,clonePos; 
        string direction; 
        cin >> cloneFloor >> clonePos >> direction; cin.ignore();
        if(((clonePos <= elevators[cloneFloor])&&(direction == "RIGHT"))
            ||((clonePos >= elevators[cloneFloor])&&(direction == "LEFT")))
        {
            cout<<"WAIT"<<endl;
        }
        else
        {
            cout<<"BLOCK"<<endl; 
        }
    }
}