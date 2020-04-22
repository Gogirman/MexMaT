// MPfirst.cpp: ���������� ����� ����� ��� ����������� ����������.
//

#include<mpi.h>
#include <cmath>  
#include <time.h>
#include<iostream>
using namespace std;

// I = integrate e^(2x) dx 1..2
// I = 23.605

int _main(int argc, char **argv) {
	int rank, size;
	const int N = 1000;
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0) {
		double time = 0;

		MPI_Request reqs[N];
		MPI_Status stats[N];
		int send = 1;

		time = MPI_Wtime();
		for (int i = 0; i < N; i++) {
			MPI_Isend(&send, 1, MPI_INT, 1, i, MPI_COMM_WORLD, &reqs[i]);
		}

		MPI_Waitall(N, reqs, stats);

		double finish = MPI_Wtime();
		time = (finish - time);
		cout << "Isend_time = " << time / N * 1000 << " (ms)" << endl;
	}

	else if (rank == 1) {
		MPI_Request reqs[N];
		MPI_Status stats[N];
		int recv[N];
		for (int i = 0; i < N; i++) {
			MPI_Irecv(&recv[i], 1, MPI_INT, 0, i, MPI_COMM_WORLD, &reqs[i]);
		}

		MPI_Waitall(N, reqs, stats);

		//for (int i = 0; i < N; i++) {
	        //cout << recv[i] << endl;
		//}
	}

	MPI_Finalize();
	return 0;
}

/*
// ����� 1
int main(int argc, char **argv)
{
int rank, size;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);
cout << "The number of processes: "
<< rank
<< " my number is "
<< size
<< endl;
MPI_Finalize();
return 0;
}*/

/*
// ����� 2
int main(int argc, char **argv)
{
int rank, size;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

if (rank == 1) {
cout << "Current rank: " << rank << endl;
char send = 'a';
MPI_Send(&send, 1, MPI_CHAR, 3, 1, MPI_COMM_WORLD);
}

if (rank == 3) {
cout << "Current rank: " << rank << endl;
char recv;
MPI_Status stat;
// ����� ������������ MPI_Prob
MPI_Recv(&recv, 30, MPI_CHAR, 1, 1, MPI_COMM_WORLD, &stat);
cout << "Message: " << recv << endl;
}

MPI_Finalize();
return 0;
}*/

/*
// ����� 3
int main(int argc, char **argv)
{
int rank, size;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

if (rank == 1) {
cout << "Current rank: " << rank << endl;
char send1 = 'a';
char recv1;
MPI_Status stat;
MPI_Ssend(&send1, 1, MPI_CHAR, 3, 1, MPI_COMM_WORLD);
MPI_Recv(&recv1, 1, MPI_CHAR, 3, 2, MPI_COMM_WORLD, &stat);
cout << "Message: " << recv1 << endl;
}

if (rank == 3) {
cout << "Current rank: " << rank << endl;
char recv2;
char send2 = 'b';
MPI_Status stat;
MPI_Recv(&recv2, 1, MPI_CHAR, 1, 1, MPI_COMM_WORLD, &stat);
MPI_Ssend(&send2, 1, MPI_CHAR, 1, 2, MPI_COMM_WORLD);

cout << "Message: " << recv2 << endl;
}

MPI_Finalize();
return 0;
}
*/

/*
// ����� 4 �������
void send(int send, int from, int to) {
cout << "Current rank: " << from << endl;
cout << "Current MESSAGE = " << send << endl;
cout << "rank_" << from << "->" << "rank_" << to << endl << endl;
send <<= 1;
MPI_Send(&send, 1, MPI_INT, to, 1, MPI_COMM_WORLD);
}

int recieve(int rank, int from) {
int recv;
MPI_Status stat;
MPI_Recv(&recv, 30, MPI_INT, from, 1, MPI_COMM_WORLD, &stat);
return recv;
}

int main(int argc, char **argv) {
int rank, size;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

int MESSAGE = 1;
for (int i = 0; i < 2; i++) {

if (rank == 0) {
if (i == 1) {
int rec = recieve(0, 4);
send(rec, 0, 1);
}
else {
send(MESSAGE, 0, 1);
}
}

else if (rank == 1) {
int rec = recieve(1, 0);
send(rec, 1, 2);
}

else if (rank == 2) {
int rec = recieve(2, 1);
send(rec, 2, 3);
}

else if (rank == 3) {
int rec = recieve(3, 2);
send(rec, 3, 4);
}

else if (rank == 4) {
int rec = recieve(4, 3);
if (i == 0) {
send(rec, 4, 0);
} else if (i == 1) {
cout << "Final MESSAGE = " << rec << endl;
}
}
}

MPI_Finalize();
return 0;
}
*/

/*
// ����� 5
int main(int argc, char **argv) {
int rank, size;
int N = 1000;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

if (rank == 0) {
double time = 0;
for (int i = 0; i < N; i++) {
double start = MPI_Wtime();
char send = 'a';
MPI_Send(&send, 1, MPI_CHAR, 1, 1, MPI_COMM_WORLD);
double finish = MPI_Wtime();
time += (finish - start);
}
cout << "Send_time = " << time / N * 1000 << " (ms)" << endl;
}

else if (rank == 1) {
char recv;
MPI_Status stat;
for (int i = 0; i < N; i++) {
MPI_Recv(&recv, 1, MPI_CHAR, 0, 1, MPI_COMM_WORLD, &stat);
}
}

MPI_Finalize();
return 0;
}
*/

/*
// ����� 7
int main(int argc, char **argv) {
int rank, size;
int N = 1000;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

if (rank == 0) {
double time = 0;
int buff_size = N * sizeof(char) + MPI_BSEND_OVERHEAD;
char* buff = (char*)malloc(buff_size);
MPI_Buffer_attach(buff, buff_size);

for (int i = 0; i < N; i++) {
double start = MPI_Wtime();
char send = 'a';
MPI_Bsend(&send, 1, MPI_CHAR, 1, 1, MPI_COMM_WORLD);
double finish = MPI_Wtime();
time += (finish - start);
}

MPI_Buffer_detach(buff, &buff_size);
free(buff);

cout << "Bsend_time = " << time / N * 1000 << " (ms)" << endl;
}

else if (rank == 1) {
char recv;
MPI_Status stat;
for (int i = 0; i < N; i++) {
MPI_Recv(&recv, 1, MPI_CHAR, 0, 1, MPI_COMM_WORLD, &stat);
}
}

MPI_Finalize();
return 0;
}
*/

/*
// ����� 8
int main(int argc, char **argv) {
int rank, size;
const int N = 1000;
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

if (rank == 0) {
double time = 0;

MPI_Request reqs[N];
MPI_Status stats[N];

for (int i = 0; i < N; i++) {
int send = 1;
double start = MPI_Wtime();
MPI_Isend(&send, 1, MPI_INT, 1, i, MPI_COMM_WORLD, &reqs[i]);
double finish = MPI_Wtime();
time += (finish - start);
}

MPI_Waitall(N, reqs, stats);
cout << "Bsend_time = " << time / N * 1000 << " (ms)" << endl;
}

else if (rank == 1) {
MPI_Request reqs[N];
MPI_Status stats[N];
int recv[N];
for (int i = 0; i < N; i++) {
MPI_Irecv(&recv[i], 1, MPI_INT, 0, i, MPI_COMM_WORLD, &reqs[i]);
cout << recv[i] << endl;
}

MPI_Waitall(N, reqs, stats);
}

MPI_Finalize();
return 0;
}
*/

/*
// ����� 9
double y(double x) {
return exp(2 * x);
}

double randomize(int a, int b)
{
return (double)rand() / (double)RAND_MAX * (b - a) + a;
}


int main(int argc, char **argv) {
int rank, size;
const int N = 40000;
double integrate = 0;
srand(time(NULL));
MPI_Init(&argc, &argv);
MPI_Comm_size(MPI_COMM_WORLD, &size);
MPI_Comm_rank(MPI_COMM_WORLD, &rank);

double sum = 0;
for (int i = 0; i < N; i++) {
double x = randomize(1, 2);
cout << x << endl;
sum += y(x);
}

MPI_Reduce(&sum, &integrate, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
MPI_Barrier(MPI_COMM_WORLD);

if (rank == 0) {
double result = (double)integrate * (2 - 1) / ((double)size*N);
cout << result << endl;
}

MPI_Finalize();
return 0;
}
*/

/*
// ����� 10

*/

/*
// ����� 11

*/