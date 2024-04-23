#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>

using namespace std;

int mod = 1000000007;
vector<vector<vector<long long>>> dp;

void solve(int n, int l, int r)
{
    dp[1][1][1] = 1;
    for (int i = 2; i < n + 1; i++)
    {
        dp[i][i][1] = dp[i][1][i] = 1;
        for (int j = 1; j < l + 1; j++)
        {
            for (int k = 1; k < r + 1; k++)
            {
                dp[i][j][k] = (dp[i - 1][j][k - 1] + dp[i - 1][j - 1][k] + (dp[i - 1][j][k] * (i - 2))) % mod;
            }
        }
    }
}

int main()
{
    ofstream bw("dish.out");
    ifstream br("dish.inp");
    stringstream sb;
    int T;
    br >> T;
    while (T-- > 0)
    {
        int n, l, r;
        br >> n >> l;
        r = n;
        dp.assign(n + 1, vector<vector<long long>>(n + 1, vector<long long>(n + 1, 0)));
        solve(n, l, r);
        long long sum = 0;
        for (int i = 0; i <= n; i++)
        {
            sum = (sum + dp[n][l][i]) % mod;
        }
        sb << n << ' ' << l << ' ' << sum << '\n';
    }
    bw << sb.str();
    bw.close();
    br.close();

    return 0;
}
