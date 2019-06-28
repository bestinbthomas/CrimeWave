package com.example.crimewave;
@FunctionalInterface
public interface OpenFragment {
    int FAVORITE = 1;
    int SEARCH_CRIMES = 2;
    int SEE_FORCES = 3;
    void LaunchFragment(int Fragref);
}
