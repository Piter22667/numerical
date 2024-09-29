#include <iostream>
#include <iomanip>
#include <cmath>
using namespace std;

double func(double x) {
    return x*x*x-2*x*x-x+2;
}

double fDerivative(double x) {
    return 3*x*x-4*x-1;
}

int iterationsCount(double x0, double epsilon, double q, double xMAx) {
    double part1 = log(fabs(x0 - xMAx) / epsilon);
    double part2 = log(1 / q);
    return static_cast<int>(log2(part1 / part2 + 1) + 1);
}

void newtonMethod(double x0, double epsilon, double q, double xMax) {
    double f_prime_x0 = fDerivative(x0);
    double x = x0;
    double x_prev;
    int step = 0;

    int maxIterations = iterationsCount(x0, epsilon, q, xMax);
    cout << setw(5) << "i" << setw(15) << "x" << setw(15) << "f(x)" << endl;

    do {
        x_prev = x;
        double fx = func(x);
        x = x - fx / f_prime_x0;
        step++;
        cout << setw(5) << step << setw(15) << x << setw(15) << fx << endl;

    } while (step <= maxIterations && fabs(x - x_prev) >= epsilon / 10);

    cout << "Result x = " << x << endl;
}

int main() {
    double x0 = 2.05;
    double xMax = 1.81123;
    double q = 0.3;
    double epsilon;

    cout << "Enter epsilon: ";
    cin >> epsilon;
    newtonMethod(x0, epsilon, q, xMax);
    return 0;
}
