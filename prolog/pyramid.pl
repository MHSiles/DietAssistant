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

/* PYRAMID GROUPS */

pyramid(group1, fruit).
pyramid(group1, vegetable).
pyramid(group2, flour).
pyramid(group2, legume).
pyramid(group3, lacteous).
pyramid(group4, protein).
pyramid(group5, fat).
pyramid(group6, junk).

% ---------------------

% FRUITS & VEGETABLES

type(fruit, apple).
type(fruit, banana).
type(fruit, orange).
type(fruit, strawberries).
type(fruit, watermelon).
type(fruit, melon).
type(fruit, pear).
type(fruit, grapes).
type(fruit, peach).
type(vegetable, carrots).
type(vegetable, tomato).
type(vegetable, lettuce).
type(vegetable, spinach).

% FLOUR & LEGUMES

type(flour, bread).
type(flour, spaguetti).
type(flour, cereal).
type(legume, rice).
type(legume, potatos).
type(legume, beans).

% LACTEOUS

type(lacteous, milk).
type(lacteous, yogurt).
type(lacteous, cheese).

% PROTEINS

type(protein, fish).
type(protein, eggs).
type(protein, beans).
type(protein, chicken).
type(protein, nuts).
type(protein, almonds).
type(protein, meat).
type(protein, sausage).
type(protein, salami).

% FATS

type(fat, oil).
type(fat, mayonnaise).
type(fat, butter).

% JUNK FOOD

type(junk, cake).
type(junk, candies).
type(junk, pie).
type(junk, nutella).
type(junk, soda).
type(junk, chips).
type(junk, chocolate).
