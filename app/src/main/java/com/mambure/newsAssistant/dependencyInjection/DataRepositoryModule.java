package com.mambure.newsAssistant.dependencyInjection;

import dagger.Module;

@Module(includes = {RoomDatabaseModule.class, NewsServiceModule.class})
class DataRepositoryModule {

}





