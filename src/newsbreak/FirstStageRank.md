final output 500 ads with highest score

score = predictedCPM = f(pctr, pcvr)

pctr = ad_embedding_ctr x user_embedding
pcvr = ad_embedding_cvr x user_embedding

user_embedding = user_tower(user_features) 
user_features: a row of a table, stored in scylla
user_embedding: List<Float>, returned by NBserving


ad_embedding_ctr = ctr_tower(ad_features)
ad_embedding_cvr = cvr_tower(ad_features)
ad_embedding :List<Float>, stored in scylla, updated hourly
