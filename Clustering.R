#load the required libraries for nlp,clustering and evaluation 
library("tm")
library("cluster")
library("factoextra")

options(header=FALSE,stringsAsFactors = FALSE)
#read the crawled dataset from csv file
data <- read.csv("treatment_input.csv")

#prepare a corpus space
corpus <- Corpus(VectorSource(data$posts))
inspect(corpus)

#perform nlp of texts
data_clean <- tm_map(corpus,stripWhitespace)
data_clean <- tm_map(data_clean,removePunctuation)
data_clean <- tm_map(data_clean,removeWords,stopwords("english"))

#term-document matrix creation
dtm <- DocumentTermMatrix(data_clean)
dtm_tf_idf <- weightTfIdf(dtm)
m <- as.matrix(dtm_tf_idf)
rownames(m) <- 1:nrow(m)
norm_eucl <- function(m) m/apply(m,1,function(x) sum(x^2)^.5)
m_norm <- norm_eucl(m)

#k-means clustering
results <- kmeans(m_norm,3,3)
options(max.print=3000)
results$cluster

#silhouette coefficient evaluation
fviz_nbclust(m, kmeans, method = "silhouette")


