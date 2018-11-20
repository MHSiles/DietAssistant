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

% HEALTHY AND UNHEALTHY FOOD GROUPS

healthy(group1).
healthy(group2).
healthy(group3).
healthy(group4).
unhealthy(group5).
unhealthy(group6).

% DAILY FRECUENCY

frecuency(group1, 6).
frecuency(group2, 4).
frecuency(group3, 3).
frecuency(group4, 2).
frecuency(group5, 1).
frecuency(group6, 0).

recommendation(group1, overobesity).
recommendation(group1, obesity).
recommendation(group1, over).
recommendation(group1, normal).
recommendation(group4, normal).
recommendation(group2, under).
recommendation(group4, under).
recommendation(group5, under).
recommendation(group6, under).

norecommendation(group2, overobesity).
norecommendation(group3, overobesity).
norecommendation(group4, overobesity).
norecommendation(group2, obesity).
norecommendation(group3, obesity).
norecommendation(group2, over).
norecommendation(group3, over).
norecommendation(group5, normal).
norecommendation(group6, normal).

moderate(group4, obesity).
moderate(group4, over).
moderate(group2, normal).
moderate(group3, normal).
moderate(group1, under).
moderate(group3, under).

avoid(group5, overobesity).
avoid(group6, overobesity).
avoid(group5, obesity).
avoid(group6, obesity).
avoid(group5, over).
avoid(group6, over).

% IMC

imc(18.5, under).
imc(25, normal).
imc(30, over).
imc(39, obesity).
imc(60, overobesity).
