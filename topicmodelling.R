#load the required libraries for nlp and topic modelling
library("tm")
library("wordcloud")
library("slam")
library("topicmodels")

#read clusters into a data frame
data <- file("treatment_cluster_3.txt")

#remove the links from posts
data_clean <- readLines(data)
data_clean <- gsub("http[^[:blank:]]+", "", data_clean)
data_clean <- gsub("[ t]{2,}", "", data_clean)
data_clean <- gsub("[[:punct:]]", "", data_clean)

#prepare a corpus space and perform nlp tasks
corpus <- Corpus(VectorSource(data_clean))
inspect(corpus)
cleanset <- tm_map(corpus,stripWhitespace)
cleanset <- tm_map(cleanset,removePunctuation)
cleanset <- tm_map(cleanset,removeWords,stopwords("english"))
inspect(cleanset)

#term-document matrix creation
tdm <- DocumentTermMatrix(cleanset)
dtm_tf_idf <- weightTfIdf(tdm)
m <- as.matrix(dtm_tf_idf)
tfidf <- tapply(tdm$v/row_sums(tdm)[tdm$i], tdm$j, mean) * log2(nDocs(tdm)/col_sums(tdm > 0))
summary(tfidf)
tdm <- tdm[,tfidf >= 0.1]
tdm <- tdm[row_sums(tdm) > 0,]
summary(col_sums(tdm))

#Deciding best K value using Log-likelihood method
best.model <- lapply(seq(2, 20, by = 1), function(d){LDA(tdm, d)})
best.model.logLik <- as.data.frame(as.matrix(lapply(best.model, logLik)))

#calculating LDA,giving the k topics and number of posts
k = 3
SEED = 27
CSC_TM <-list(VEM = LDA(tdm, k = k, control = list(seed = SEED)),VEM_fixed = LDA(tdm, k = k,control = list(estimate.alpha = FALSE, seed = SEED)),Gibbs = LDA(tdm, k = k, method = "Gibbs",control = list(seed = SEED, burnin = 1000,thin = 100, iter = 1000)),CTM = CTM(tdm, k = k,control = list(seed = SEED,var = list(tol = 10^-4), em = list(tol = 10^-3))))
 
sapply(CSC_TM[1:2], slot, "alpha")
sapply(CSC_TM, function(x) mean(apply(posterior(x)$topics, 1, function(z) - sum(z * log(z)))))
topic <- topics(CSC_TM[["VEM"]], 1)
terms <- terms(CSC_TM[["VEM"]], 4)

#get the topics best describing the cluster
terms
