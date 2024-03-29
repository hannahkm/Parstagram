# Project 4 - *Parstagram*

**Parstagram** is a photo sharing app like Instagram using Parse as its backend.

Time spent: **12** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in to his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can log out of his or her account
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to go to a Post Details activity, which includes timestamp and caption.
- [X] User sees app icon in home screen

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [X] User should switch between different tabs using fragments and a Bottom Navigation View.
  - [X] Feed Tab (to view all posts from all users)
  - [X] Capture Tab (to make a new post using the Camera and Photo Gallery)
  - [X] Profile Tab (to view only the current user's posts, in a grid)
- [X] Show the username and creation time for each post
- User Profiles:
  - [X] Allow the logged in user to add a profile photo
  - [X] Display the profile photo with each post
  - [X] User Profile shows posts in a grid
- [X] User can like a post and see number of likes for each post in the post details screen.

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. How can we best customize the action bar with images and left/center aligned icons?
2. The most cost/time efficient way to get data from Parse

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/hannahkm/Parstagram/raw/master/ParstagramGif1.gif' title='Logging in and viewing posts' width='200' alt='Video Walkthrough pt 1' />
<img src='https://github.com/hannahkm/Parstagram/raw/master/ParstagramGif2.gif' title='Making a post and logging out' width='200' alt='Video Walkthrough pt 2' />

GIF created with [Kap](https://getkap.co/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

* At first, I implemented the navigation bar using image buttons. In order to add the navigation bar with fragments, I had to refactor most of my code. Luckily, it went with only a few bumps!
* I had trouble adding images as circles, as RoundedCornersTransformation only rounded the image up to a certain amount. This was resolved when I found the Glide function circleCrop().

## License

    Copyright 2021 Hannah Kim

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
