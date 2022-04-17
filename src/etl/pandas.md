## Pandas

### Basic

```
df.shape()
df.head(10)
df.median()
df.describe()
```

### Change column names
```
df.columns = [x.upper() for x in df.columns]

df.columns = df.columns.str.replace(' ', '_')

df.rename(columns={'first_name': 'first', 'last_name': 'last'}, inplace=True)

df.rename(columns={df.columns[1]:"log"}, inplace=True)
```

### Update row
```
df.loc[2] = ['John', 'Smith', 'js@email.com'] // update entire row
df.loc[2, ['last', 'email']] = ['Doe', 'johndoe@email.com'] // only update part of columns of a row
df.loc[2, 'last'] = 'Smith' // only update one column of a row
df.at[2, 'last'] = 'Smith' // Same as above

// create a filter
// update values matching the filter 

// make the value of entire 'email' column lower case

```

### four confusing methods 

- `apply` Works on either a Serious or a DF, the input is actually treated as a single-dimension vector and it is like applying the function on each of element of this one-dimension vector (DF is considered as a list of Serious).
```
// apply a named function to an entire column

// apply a lambda function to an entire column
```

```
df['email'].apply(len)
df.apply(len)
df.apply(len, axis='columns')

// get min value in each column
// get min value in each column by lambda
```

- `applymap` Only works on DF, applies the function on each of individual element in the data frame
```
df.applymap(len)
```


- `map` Only works on Serious, substitute each element in a serious by another value.
```
df['first'].map({'Corey':'Chris', 'Jane':'Mary'}) // in this case Corey and Jane will be changed to Chris and Mary respectively, but other elements in this Serious will be changed to NaN.
```

- `replace`, if you wanna avoid other elements to be changed, you can use this method.

### update columns

```
df['full_name'] = df['first'] + ' ' + df['last']
```

```
df.drop(columns=['first', 'last'])

df['full_name'].str.split(' ')
df['full_name'].str.split(' ', expand=True)
df[['first', 'last']] = df['full_name'].str.split(' ', expand=True)
```

```
df.append({'first': 'Tony'}, ignore_index=True) // append a row
df.append(df2, ignore_index=True) // append df2 to df

df.drop(index=4) // drop the fourth row
df.drop(index=df[df['last] == 'Doe'].index)
```

### sorting

```
df.sort_values(by='last', ascending=False)
df.sort_values(by=['last', 'first'], ascending=[False, True], inplace=True)
df.sort_index()
df['last'].sort_values()
df['ConvertedComp].nlargest(10)
df.nsmallest(10, 'ConvertedComp')
```

### grouping and aggregating

```
df['Hobbyist'].count()
df['Hobbyist'].value_counts()
df['Hobbyist'].value_counts(normalize=True)
```

```
fltr = df['Country'] == 'United States'
df.loc[fltr]['SocialMedia'].value_counts()


country_grp = df.group_by(['Country']) // return a GroupBy object
country_grp.get_group('United States)
country_grp['SocialMedia'].value_counts() // return a Serious object
country_grp['SocialMedia'].value_counts().loc['India']
country_grp['ConvertedComp'].agg(['median', 'mean'])
```

```
df['Hobbyist'].value_counts(normalize=True)
```

```
fltr = df['Country'] == 'United States'
df.loc[fltr]['LanguageWorkedWith'].str.contains('Python')

country_grp = df.group_by(['Country']) // return a GroupBy object
country_grp['LanguageWorkedWith'].apply(lambda x: x.str.contains('Python').sum())
```

### joining

```
country_resp = df['Country'].value_counts()

country_grp = df.group_by(['Country']) // return a GroupBy object
country_grp['LanguageWorkedWith'].apply(lambda x: x.str.contains('Python').sum())

new_df = pd.concat([country_resp, country_use_python], axis='columns')
```

### Cleaning data, handle missing data

```
import pandas as pd
import numpy as np

df.dropna() // drop NaN or empty string rows
df.dropna(axis='index', how='any') // same as above, default values, drop a row if any column has missing value
df.dropna(axis='index', how='all') // drop a row if all columns in this row have missing value
df.dropna(axis='index', how='all', subset=['email', 'last']) // drop a row if both email and last name are missing

df.isna()
df.fillna('MISSING') // replace na with MISSING
```

#### casting
```
df['age'] = df['age'].astype(float)
```
