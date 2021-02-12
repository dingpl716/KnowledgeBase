GIT_BRANCH=$(strip $(shell git symbolic-ref --short HEAD))

create-pr:
	@echo "Creating pull request..."
	@git push --set-upstream origin $(GIT_BRANCH)
	@hub pull-request

browse-pr:
	@hub browse -- pulls

changelog:
	@git add CHANGELOG.md
	@git commit -m "Update change log."
