Read me:

I run my program in eclipse in windows. Please create a java project in eclipse and run main function under kmean.java. 

Under kmean.java. I have several configurations of input data. The one configuration that is used for my program to directly run is like following. copy your "A.easy.csv", "A.hard.csv", and "wine.csv" (note that in project description, it said that you need to remove class column if you are using homework 4 data) into your eclipse project root directory or a path (if it is another path, please modify the path inside kmean.java). 

The output will be output1, output2, output1-1, output2-1, and output3.
They are k = 2, dataset1; k = 4, dataset2; k = 3, dataset1; k = 3, dataset2; k = 10, wine data respectively. Note that you can uncomment the other output method in kmean.java in order to get the x, y coordinations of the points.

The general outputs of this program are shown on the console. The output has the format:

True clustering
Cluster 1 SSE,0.9343135635537495
Cluster 2 SSE,6.723954566951231
Total k cluster SSE,7.658268130504981
Total SSB,30.453557381589476
Silhouette in 1 cluster,0.8406078139443299
Silhouette in 2 cluster,0.5854931739716334
Total Silhouette,0.7130504939579816
K mean clustering
Cluster 1 SSE,6.450014703071413
Cluster 2 SSE,1.10134235226528
Total k cluster SSE,7.551357055336693
Total SSB,30.56046845675778
Silhouette in 1 cluster,0.5958135981606236
Silhouette in 2 cluster,0.8293417058648586
Total Silhouette,0.7125776520127411
138,0
2,160

the last two lines represents confusion matrix.
For input files, I have three configurations in kmean.java in order to use class label in wine and so on...