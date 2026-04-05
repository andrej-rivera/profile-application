# 3/26
As of right now, the postgresDB runs locally on my andre computer. It's a basic postgresDB and it runs on port 5432 as specified in the application.yaml file.
Later, we can potentially use docker to run the db instead for a more consistent approach

You can access the db via
`psql -d spring_react_project`
`\dt` shows all tables in the db