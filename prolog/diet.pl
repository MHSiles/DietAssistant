% // Copyright 2018, Mauricio Hernández
%
% // This file is part of Diet Assistant.
% //
% // Diet Assistant is free software: you can redistribute it and/or modify
% // it under the terms of the GNU General Public License as published by
% // the Free Software Foundation, either version 3 of the License, or
% // (at your option) any later version.
% //
% // Diet Assistant is distributed in the hope that it will be useful,
% // but WITHOUT ANY WARRANTY; without even the implied warranty of
% // MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
% // GNU General Public License for more details.
% //
% // You should have received a copy of the GNU General Public License
% // along with Diet Assistant.  If not, see <https://www.gnu.org/licenses/>.

:- [pyramid].
:- [health].
:- [language].
:- [personalIMC].
:- [personalPreference].
:- [personalNoPreference].

% GET THE IMC OF THE PERSON

  getIMCStatus(IMC, Ans):-
    imc(Value,Status),
    IMC < Value,
    Ans = Status,
    !.

% IDENTIFY WHAT THE USER IS ASKING

   readPhrase([H|_], Ans):-
      (isEqual(H,i),
      Ans = statement,
      !);
      (isEqual(H,is),
      Ans = health,
      !);
      (isEqual(H,can),
      Ans = question,
      !);
      (isEqual(H,recommend),
      Ans = recommendation,
      !);
      (isEqual(H,how),
      Ans = frecuency,
      !);
      Ans = error.

% 1. I "LIKE/DONT LIKE" ________.

   % GETS THE PHRASE TO IDENTIFY A NEGETION OR NOT
   statement([H|T], Negation, Preference, Food):-
      (negation(H),
      Negation = true,
      getPreference(T, Preference, Food),
      !);
      % IF ITS A VERB, THERE WILL BE NOT NEGATION AND CONTINUES EXECUTING
      (verb(_, H),
      getPreference([H|T], Preference, Food),
      Negation = false,
      !);
      % IF NEITHER, LOOPS
      statement(T,Negation, Preference,Food).

   % GET IF THE USER LIKES OR DISLIKES THE FOOD
   getPreference([H|T], Preference, Food):-
      (verb(Preference, H),
      identifyFood(T, Food),
      !);
      getPreference(T, Preference, Food).

    previouslySaid(Preference, Predicate1, Predicate2):-
      (isEqual(Preference, preference),
      predicateExists(Predicate1),
      call(Predicate1),!);
      (isEqual(Preference, nopreference),
      predicateExists(Predicate2),
      call(Predicate2), !).

% 2. IS _________ HEALTHY/UNHEALTHY?

   % GET IF CERTAIN FOOD IS HEALTHY OR UNHEALTHY BASES ON THE Q's
   identifyFoodHealth([H|T]):-
      (
         % GET THE TYPE OF THE FOOD
         type(_, H),
         % GET IF IT IS HEALTHY OR NOT
         healthyFoodType(H, FoodHealth),
         % GET WHAT THE USER IS ASKING
         getQuestion(T, Question),
         % COMPARE
         Question = FoodHealth,
         !
      );
      identifyFoodHealth(T).

   % GET IF FOOD IS HEALTHY
   healthyFoodType(Food, Response):-
      (pyramid(Group, Type),
      type(Type, Food),
      healthy(Group)),
      Response = healthy.

   % GET IF FOOD IS UNHEALTHY
   healthyFoodType(Food, Response):-
      (pyramid(Group, Type),
      type(Type, Food),
      unhealthy(Group)),
      Response = unhealthy.

   % GET WHAT THE USER IS ASKING
   getQuestion([H|T], Response):-
      (isEqual(H, healthy),
      Response = H,
      !);
      (isEqual(H, unhealthy),
      Response = H,
      !);
      getQuestion(T, Response).

% 3. CAN I EAT _________?

   suggestion(Sentence, Suggestion):-
      (identifyFood(Sentence, Food),
      predicateExists(nopreference(Food)),
      call(nopreference(Food)),
      Suggestion = dislike,
      !);
      (identifyFood(Sentence, Food),
      predicateExists(preference(Food)),
      call(preference(Food)),
      Suggestion = like,
      !);
      identifyFood(Sentence, Food),
      foodGroup(Food, Group),
      imcStatus(IMC),
      getSuggestion(Group, IMC, Suggestion).

   getSuggestion(Group, IMC, Suggestion):-
      (recommendation(Group, IMC),
      Suggestion = sure, !);
      (norecommendation(Group, IMC),
      Suggestion = no, !);
      (moderate(Group, IMC),
      Suggestion = yes, !);
      (avoid(Group, IMC),
      Suggestion = never, !).

   foodGroup(Food, Group):-
      type(Type, Food),
      pyramid(Group, Type).

% 4. RECOMMEND ME SOMETHING TO EAT.

getRecommended(Flag, IMC, Group, Food):-
  (isEqual(Flag, true),
  recommendation(Group, IMC),
  getGroupFood(Group, Food));
  (isEqual(Flag, false),
  recommendation(Group, IMC),
  getGroupFood2(Group, Food)).

% 5. HOW FRECUENTLY CAN I EAT _______?

  eatFrecuency(Food, Preference, Group, Frecuency):-
    type(Type, Food),
    pyramid(Group, Type),
    frecuency(Group, Frecuency),
    ((predicateExists(preference(Food)),
    call(preference(Food)),
    Preference = true, !);
    Preference = false, !).


% SUPPORT FUNCTIONS

  % COMPARE IF TWO VALUES ARE EQUAL
  isEqual(A,B):-
     A=B.

  % GETS THE FOOD
  identifyFood([H|T], Food):-
     (type(_,H),
     Food = H),
     !;
     identifyFood(T, Food).

  % IDENTIFIES IF A PREDICATE EXISTS
   predicateExists(Predicate):-
     current_predicate(_, Predicate).

  % GETS THE FOOD OF A CERTAIN GROUP WITHOUT THE THINGS THE USER DON'T LIKE
   getGroupFood(Group, Food):-
     pyramid(Group, Type),
     type(Type, Food),
     not(nopreference(Food)).

  % GETS THE FOOD OF A CERTAIN GROUP
   getGroupFood2(Group, Food):-
     pyramid(Group, Type),
     type(Type, Food).

% --------------------

%

% getNoRecommended(Flag, IMC, Group, Food):-
%   (isEqual(Flag, true),
%   norecommendation(Group, IMC),
%   getGroupFood(Group, Food));
%   (isEqual(Flag, false),
%   norecommendation(Group, IMC),
%   getGroupFood2(Group, Food)).
%
% getModerate(Flag, IMC, Group, Food):-
%   (isEqual(Flag, true),
%   moderate(Group, IMC),
%   getGroupFood(Group, Food));
%   (isEqual(Flag, false),
%   moderate(Group, IMC),
%   getGroupFood2(Group, Food)).
%
% getAvoid(Flag, IMC, Group, Food):-
%   (isEqual(Flag, true),
%   avoid(Group, IMC),
%   getGroupFood(Group, Food));
%   (isEqual(Flag, false),
%   avoid(Group, IMC),
%   getGroupFood2(Group, Food)).
