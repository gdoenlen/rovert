Rovert is a simple slack bot that will react to your favorite user with an emoji!

### Developing
Just run `./mvnw compile quarkus:dev` and you'll be good to go!

### Production
First you'll need to set a few environment variables to get running:
 * SLACK_API_TOKEN (the token to send with slack web api requests)
 * SLACK_SIGNING_SECRET (the secret key you get when registering your bot, this will be used to authenticate requests and make sure they're from slack)
 * ROVERT_MESSAGES_USER (the user you want to react to)

Now you can just run the jar or executable!
