#include<iostream>
using namespace std;
bool dp[35][35][35];
bool c[35][35][35];
int r[15],g[15],b[15],r2[15],g2[15],b2[15];
int n;
void solve(int R,int G,int B){
    if(R<0||G<0||B<0)return;
    c[R][G][B]=true;
    dp[R][G][B]=true;
    for(int i=1;i<=n;i++){
        if(R>=r[i]&&G>=g[i]&&B>=b[i]){
            if(R-r[i]+r2[i]<=30&&G-g[i]+g2[i]<=30&&B-b[i]+b2[i]<=30){
                if(!c[R-r[i]+r2[i]][G-g[i]+g2[i]][B-b[i]+b2[i]]){
                    solve(R-r[i]+r2[i],G-g[i]+g2[i],B-b[i]+b2[i]);
                }
            }
        }
    }
}
int main(){
    int R,G,B,R2,G2,B2;
    cin>>R>>G>>B>>R2>>G2>>B2;
    cin>>n;
    for(int i=1;i<=n;i++)cin>>r[i]>>g[i]>>b[i]>>r2[i]>>g2[i]>>b2[i];
    solve(R,G,B);
    cout<<((dp[R2][G2][B2])?"YES":"NO");
    return 0;
}