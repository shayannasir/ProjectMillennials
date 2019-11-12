package com.shayannasir.projectm;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                GameFragment gameFragment = new GameFragment();
                return gameFragment;
            case 1:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case 2:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;
            case 3:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;
            default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "birb";
            case 1:
                return "Chats";
            case 2:
                return "Groups";
            case 3:
                return "Contacts";
            default:
                return null;
        }
    }
}
