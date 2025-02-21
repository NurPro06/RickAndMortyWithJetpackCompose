package kg.geeks.rickandmortywithjetpackcompose.ui.module

import kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.CharactersViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.character.detail.CharacterDetailViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.EpisodesViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.episode.detail.EpisodeDetailViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.favorite.FavoriteViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.LocationViewModel
import kg.geeks.rickandmortywithjetpackcompose.ui.screens.location.detail.LocationDetailViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule: Module = module {
    viewModel { CharactersViewModel(get()) }
    viewModel { LocationViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
    viewModel { LocationDetailViewModel(get()) }
    viewModel { EpisodesViewModel(get()) }
    viewModel { EpisodeDetailViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}