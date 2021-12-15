User stories - these user stories are subject to change, adding and deleting as well.

As a pet clinic employee I would like to be able to add a new pet to our system.
The pet can have a picture or not, an owner -mandatory - [the owner MUST have a phone
number and an address], the type of pet, his/her name.
The owner can be a previous pet owner that came to our clinic with a different pet.

As a pet clinic employee I would like to add a treatment to an already existing pet. The
treatment will have a description, a cost, the date etc.

As a pet clinic employee I would like to be able to search for a pet/pets by it’s name, owner’s
telephone number, treatment interval.

As a pet clinic employee I would like to update the information of a pet /owner (change of
address, change of telephone, change of picture, change of name).

As a pet clinic employee I would like to delete a pet from our registry. I want all the log of
it’s treatments to be deleted but I do not want the owner’s info to be deleted with it.

As a pet clinic customer I would like to receive a report of all the treatments and the costs of one
of my pets or all of my pets in a given time frame.

I do not want to have two users with the same username.

I do not want to be able to change my username to a username that already exists.

When I retrieve owners I do not want to get all the pets and all their treatments as well - I just
want a list of owners.

I want to be able to GET pets that were treated between two dates. For example - I want a list
of all the pets that were treated in our clinic between 1st of march and 10th of march.

I want to be able to use multiple filters for pets.

We need to encrypt all the passwords and save them encrypted in the db.
If two users have the same password I do not want the same encrypted text to be saved in the db.

Our marketing department decided to make a campaign every month to reward our customers
and encourage them to come to our clinic more for check-ups. Also it will increase the traffic to
our website which they think will help us for better SEO ranking. The pets that came for a visit to
our clinic in that month get enrolled in that month’s contest and they can win prizes like promo
codes for pet food or reductions to general treatments of check-ups.. This month’s contest is
called “Who’s a good boy” . All male dogs that come to our clinic this month will automatically
be enrolled in the campaign.

I want a public page accessible at /whosagoodboy where I can a galery of all the dogs that are
participating to this month’s contest - I want to see for each dog the picture, the name and the id
of that dog.

As an owner I want to be able to vote for a dog. I am only allowed to vote once per day.
At the end of the contest the owner of the dog with most votes will win a prize.

Implementation of the gallery, the voting system, the checks for voting once a day are
mandatory and only owners are allowed to vote.

Implementation of the page /winner where we will see the winner of the contest and the number
of votes he received.
