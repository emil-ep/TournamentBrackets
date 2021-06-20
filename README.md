# TournamentBracketLib
A library to generate tournament brackets with smooth transition and animations.

Find the Library repository here : https://github.com/Emil333/TournamentBracketLib


https://user-images.githubusercontent.com/32976134/122669203-d7966c00-d1d9-11eb-8471-e88af082aeb3.mp4


## Use Cases of TournamentBracketLib
- Include a layout for tournament brackets in your project
- provides customisation options for background color, bracket color and text colors

## Find this project useful? ❤️
-  support it by providing a ⭐️ on the upper right of this page.

## Requirement
-  TournamentBracketLib can be included in Android version 7 (Nougat) and above

## Using TournamentBracket in your application
-  Add this to your project level build.gradle


   ```
   allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ````
  ````
- Add the dependency

  ```
  dependencies {
	        implementation 'com.github.Emil333:TournamentBracketLib:1.0.1'
	}
  ```
  
- Add a `BracketsView` component in your XML file

  ```
  <com.ventura.bracketslib.BracketsView
        android:id="@+id/bracket_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:bracketBackgroundColor="#1c222e"
        app:bracketColor="#262e40"
        app:bracketTextColor="#fff" />
        
  ```
- Initialise your BracketsView 
  ```
  BracketsView bracketsView = findViewById(R.id.bracket_view);
  
  ```
- Provide the necessary data to populate the Brackets
  - Breaking down the Attributes
    - `CompetitorData` corresponds to an object with the necessary team/player data. Takes in `name` and `score` as parameters.
       ```
        CompetitorData brazilSemiFinal = new CompetitorData("Brazil", "3");
        CompetitorData englandSemiFinal = new CompetitorData("England", "1");
        CompetitorData argentinaSemiFinal = new CompetitorData("Argentina", "3");
        CompetitorData russiaSemiFinal = new CompetitorData("Russia", "2");
        CompetitorData brazilFinal = new CompetitorData("Brazil", "4");
        CompetitorData argentinaFinal = new CompetitorData("Argentina", "2");
       
       ```
       
    - `MatchData` corresponds to an object with the necessary competitor informations for a particular Match. Takes in two `CompetitorData` as parameters(Team1, Team2).
      ```
        MatchData match1SemiFinal = new MatchData(brazilSemiFinal, englandSemiFinal);
        MatchData match2SemiFinal = new MatchData(argentinaSemiFinal, russiaSemiFinal);
        MatchData match3Final = new MatchData(brazilFinal, argentinaFinal);
      ```
    - `ColomnData` corresponds to list of matches that needs to be displayed in a section (Semi final, final etc). Takes in a list of `MatchData` as parameter.

      ```
        ColomnData semiFinalColomn = new ColomnData(Arrays.asList(match1SemiFinal, match2SemiFinal));
        ColomnData finalColomn = new ColomnData(Arrays.asList(match3Final));
      ```
    
- call the `setBracketsData(List<ColomnData> sectionList)` methods from `bracketsView`
  ```
  bracketsView.setBracketsData(Arrays.asList(semiFinalColomn, finalColomn));
  
  ```
  
- All set 👏🏻


  
