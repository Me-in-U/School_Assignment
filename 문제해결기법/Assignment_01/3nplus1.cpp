#include <fstream>
#include <vector>

using namespace std;

const int MAX = 1000000;
vector<int> cycleLength(MAX + 1, 0);

int calculateCycleLength(int n)
{
    if (n < MAX && cycleLength[n] != 0)
    {
        return cycleLength[n];
    }
    int originalN = n;
    int cycle = 1;
    while (n != 1)
    {
        if (n % 2 == 0)
        {
            n /= 2;
        }
        else
        {
            n = 3 * n + 1;
        }
        cycle++;
        if (n < MAX && cycleLength[n] != 0)
        {
            cycle += cycleLength[n] - 1;
            break;
        }
    }
    if (originalN < MAX)
    {
        cycleLength[originalN] = cycle;
    }
    return cycle;
}

int findMaxCycleLength(int i, int j)
{
    if (i > j)
    {
        swap(i, j);
    }
    int maxCycleLength = 0;
    for (int k = i; k <= j; k++)
    {
        int length = calculateCycleLength(k);
        if (length > maxCycleLength)
        {
            maxCycleLength = length;
        }
    }
    return maxCycleLength;
}

int main()
{
    ifstream inp("3nplus1.inp");
    ofstream out("3nplus1.out");
    int i, j;
    while (inp >> i >> j)
    {
        int maxCycle = findMaxCycleLength(i, j);
        out << i << " " << j << " " << maxCycle << endl;
    }
    return 0;
}
