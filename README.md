### Instructions to run the application of DevOps Task - 1 

    1. Pull Image : sudo docker pull ashrafulsohag/repotracker:latest
    2. Run Image : sudo docker run -d -p 8081:8080 ashrafulsohag/repotracker:latest
    3. Hit API to see all the branches : http://localhost:8081/branches
    4. To view the commits : click on each branch name to see the commits of that branch

    public repo used : https://github.com/mmumshad/kubernetes-the-hard-way
    from where i pull all the branches and commits informations
