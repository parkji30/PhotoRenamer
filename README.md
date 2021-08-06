# Photo Renamer
A photo renaming application that allows the user to add tags to images and/or chooses to rename them. 

This program can be run in either a GUI or via terminal command. 

A user can add "tags" to an image file which allows the user to search any image using command line or other search 
tools via the tags added.

# ===== How To Run The App =====

1) In a terminal change directory to the src folder
2) Now enter javac photoRenamer/StartView.java
3) Now enter java phase1.StartView
4) The program should now be open

# ==== How to work the App ====

1) You must first select a directory by using the 'Find Directory' button or by selecting a currently existing tag from the list
on the left then click the 'Search By Selected Tag' button.

2) Now the list of images for the Directory or tag selected will now populate the list under 'Results'. Select a image from
this list and click 'Select Image'.

3) The image will now be displayed in the app. At this point you can click 'Sync' to update the images 'Tag List' on the right.

4) To add tags simply follow the prompt text format in the input box then click 'Add Above Tags'. To delete tags simply
select a tag from the images 'Tag List' then click 'Delete selected tag'. IMPORTANT: When making adding or deleting tags
you MUST press sync after each set of additions and each deletion in order to update the name.

5) You can also open the images Log File using 'Open Image Log' and also open the images directory by clicking 'Open Image Directory'.

6) To move an image click 'Move Image' then select a destination directory.

7) Below the Image display there is a list of all the previous names for the Image. Simply select a name and click
'Rename to selected old name' the clicking sync.

8) When quitting the app you must use the 'Exit' button in order to 'save'.

NOTE: After changing an Image's name you must either reselect the same directory using 'Find Directory' if you wish to select this
image again from the list of Images.
