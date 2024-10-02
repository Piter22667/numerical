#include <iostream>
#include <cmath>
#include <iomanip>
using namespace std;

long double f(long double x) {
    return x * x * x + 2 * x * x - x - 2;
}

long double fDerivative(long double x) {
    return 3 * x * x + 4 * x - 1;
}

int main() {
    long double x = -0.75;
    long double tau = 24.0 / 31.0;
    long double epsilon;
    int iterations = 31;
    long double f_x;
    long double m1 = 0.25;
    long double M1 = 2.333;
    long double q = (M1 - m1) / (M1 + m1);
    long double z0 = 0.75;
     cout << "Enter epsilon: ";
    cin >> epsilon;

    int calculatedIterations = ceil(log(z0 / epsilon) / log(1.0 / q)) + 1;
    cout << "Number of iterations: " << calculatedIterations << endl;
    cout << setw(8) << "i" << setw(30) << "x" << setw(30) << "f(x)" << endl;
    cout << setprecision(15);

    for (int i = 1; i <= iterations; ++i) {
        f_x = f(x);
        cout << setw(8) << i << setw(30) << x << setw(30) << f_x << endl;
        x = x + tau * f_x;

        if (i == iterations) {
            cout << "\nResult after maximum iterations  x = " << x << endl;
        }
    }

    return 0;
}
