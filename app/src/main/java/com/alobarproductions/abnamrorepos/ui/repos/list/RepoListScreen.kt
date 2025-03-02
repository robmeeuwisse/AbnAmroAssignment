package com.alobarproductions.abnamrorepos.ui.repos.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alobarproductions.abnamrorepos.R
import com.alobarproductions.abnamrorepos.core.Repo
import com.alobarproductions.abnamrorepos.main.MainModule
import com.alobarproductions.abnamrorepos.ui.AbnAmroShield
import com.alobarproductions.abnamrorepos.ui.theme.AbnAmroReposTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel(
    private val getReposByPage: suspend (pageNumber: Int) -> List<Repo> = MainModule.reposRepository::getByPage,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var lastPageNumber = 0

    fun onLaunched() {
        onLoadMore()
    }

    fun onLoadMore() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
            )
            val newPage = getReposByPage(++lastPageNumber)
            val oldRepos = _uiState.value.repos
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                repos = oldRepos + newPage,
            )
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val repos: List<Repo> = emptyList(),
    )
}

@Composable
fun RepoListScreen(
    onRepoClick: (repoId: Long) -> Unit,
) {
    val viewModel: RepoListViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    RepoListScreen(
        uiState = uiState,
        onRepoClick = onRepoClick,
        onLoadMore = viewModel::onLoadMore,
    )
    LaunchedEffect(Unit) {
        viewModel.onLaunched()
    }
}

@Composable
private fun RepoListScreen(
    uiState: RepoListViewModel.UiState,
    onRepoClick: (repoId: Long) -> Unit,
    onLoadMore: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { RepoListTopBar() },
    ) { innerPadding ->
        RepoListContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            uiState = uiState,
            onRepoClick = onRepoClick,
            onLoadMore = onLoadMore,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RepoListTopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.repos_screen_title)) },
        navigationIcon = {
            AbnAmroShield(
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp),
            )
        },
    )
}

@Composable
private fun RepoListContent(
    uiState: RepoListViewModel.UiState,
    onRepoClick: (repoId: Long) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        itemsIndexed(
            items = uiState.repos,
            key = { index, repo -> repo.id },
        ) { index, repo ->
            if (index == uiState.repos.lastIndex) {
                onLoadMore()
            }
            RepoListItem(
                repo = repo,
                onClick = { onRepoClick(repo.id) },
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AbnAmroReposTheme {
        RepoListScreen(
            uiState = RepoListViewModel.UiState(
                isLoading = true,
                repos = List(2) { index ->
                    Repo(
                        id = index.toLong(),
                        name = "Repo $index",
                        fullName = "Group/Repo $index",
                        description = null,
                        ownerAvatarUrl = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEhUSExMVEhUTFRUYGBcYFRUXFRUXFhUWFhUVFRUYHSggGBolGxcVITEhJSkrLi4uFx8zODMtNyguLisBCgoKDg0OGxAQGy0lICUuMC4tKzctLSsvMCsrLS0vLS0tLS0uLS0tLy0tLS0tLS0tLi0tLS0tLTUvLS0tLS0tLf/AABEIAOQA3QMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAAAAQYHAgQFAwj/xABGEAABAgMEBQoEBAMGBgMAAAABAAIDESEEEjFBBQYiMvATQlFhYnGBkaHRB1KxwRQjcoIzkqIVQ1Oys+Fzo8LS4vEWVIP/xAAaAQEAAwEBAQAAAAAAAAAAAAAAAwQFAgEG/8QALBEAAgICAQMDAgYDAQAAAAAAAAECAwQRIRIxQRMiUTJCBWFxgbHwM1KRI//aAAwDAQACEQMRAD8Aupzr1Ai9IXc0OlzcUUl2vVADTcxzSa27tZe6be16pCeeHEkAFszey9k3G/hkkZ5YcTTd2fRABdMXc/ZDXXaFBlKm969aGy52KATRdqc0Xa3skN7WCM+z6IAcL+GSbnXqBeVptLIdS9rR1kD6rnx9YbM3deXHstJ9TRdxrnL6U2cSsjHuzqh0hdz90N2Mc1Hn61wsobyekyHuvH/5d0wZ/v8A/FSrEuf2/wAEbyavkkwbI3svdDm3qhRpmto50Iy6ng/ZbEPWqF8r2juBH1R4ty+0LJqfk7zjfoMkXqXc8Fz4WmrO7ciAHrm3/Mt8EETEiVDKEo/UtEsZRl2Y2m5Q5pNbdqU29r1Sb2sFydAWzN7L2Tdt4ZJHGm76dabqbvjJABdMXc/ZAddoUGWW9xNDZc7FAJouY5olW9km3teqWfZ9EAOF6oyWXLjrWLuzgspM6vNAItuVxRdnteKTRdqfdBFb2XGSAYF/qkkHXtniibhe3fZBM6DFAK9d2eKpnY65oBlQ4pN2d73QDuy2uKoAvVwWlpHSMOALzzjg0bx7h91ENK6biRiRuM+UZ/qOasU407eey+SC3IjXx5JJpLWOEyg23DJpp4u9pqPWzWGO8SDuTb0Nx8XY+Ulw7RaWs3j4ZnwXLtGlnGjRdHTifYLTrxa4eNv8yhPIsn50dmNE5zj4k/crUiaShjMu7h9yuE95JmSSeszWM1Z0Q6Os/THQzzPsvI6Yfk1vr7rmzRNe6B0P7XidDfI+6ybph+bW+vuubNE00DsM0yM2HwM/qt+wacDTNkR0M+IHjl5qMTTmvGk1phcdizLFrS4y5QCIPmbIHywPopJYdIw44kxwplzh3gqkYUVzatJHcunY9NPaQXZc5tHDjwVO3ChLlcMsV5M49+UXJels8VTOx1zUR0FrcHC7EIcPnG8P1t+49VK4EQSDphwdUEGYPisy2mdb1I0K7Y2LaMy2W1xVAbe2uKJASMzghwnUYKIkGDf6pJXuYm7a3fZE6Xc+M0AiblMZp/h+tDTdoVjyTuCgG0k72Hkgmsubxmnev0wRels+E0AnU3fdMiVRiidzrmldu7XFUAwJ1OK42m9OCELo2omQybPN3slrDpkQhJv8R2A+UfMfsFCY0XF7z1klXsXF6/fPt/JTyMjp9se56WiO55L3umTiTxQLj2zSmUP+b2H3Wrb7cYlBRvR09Z9lpFaqRn/qZOcSZkzPqsZoSXQBCEL0AhCEAIQhACEIQAnNJCAzhvIMwZFSfV3Wl0Ehr6tJqMj1j5T6KKpzXE4KS0zqMnF7RedhtrYzQ5pmw+YPQegr3JIoMFT+rmsL7M/5mHeb1ccZK2bBbmRIbXwzea8TB78iOkLFycd1PjsadFysXPc2HU3fdEqT53GSJXOuaLvP9FWJwaJ72Kx5R3AWUr9cJI/EdSAHEHdx8kTEpc7jNDhdqEXaXs0ANpve60tK28QGF7qzo1vScvBbo2qupJQHT2kTHiznsNowdXzd59lZxqfVnz2XcgyLfTjx3ZoWmOXFz3mpmSeMAo7b7aYhkKNGA6esrY0ta5m4DQY9Z6PBcslbaRlDXf0XqjaLRCbFaYbWunIOc4OMiROQaaTBzUfV5aLsYhwYcPDk2Nb/ACgA+qq5d7qS6e7LGPUrG9lT6S1WtcATfCJb8zCHj0qPELiq+mm9Qri6a1Zs9pO0y4//ABGUd+7J3ioK/wAQ8TX/AAmnh/6sp5CkOmtUrTZ3SDDGaTsuhtLp/qaJlp9OtFm1NtrxPkrg6XuaP6QSfRXvWr1vqRU9Oe9aZHkKawPh3HIm6NCb3BzvqAtiF8OCcbSB/wDkf+9RvLpX3fydrHs+CBIU6d8OnTkLQ098Mj/qK07TqBam7roT+5zgfVsvVerKqf3HjosXgiKcl17Zq1bIW9Aeetovj+iakGgNQ3PAiWklgxEMHbP6zze4V7l7PIrjHqbPI1Tk9JENs9nfEddY1z3HJoJPkFILNqPbHCbmshfrfXyYHKzdG6OhQmXIbGw2jJolPrJxJ6ythpvUOCoT/EJP6UW4Ya+5lG6wWQ2OKIMYi8Wh4LbxaQSRQyGYIwWpCitdukHuKl/xxsoEOzxgN2I6H3h7b4n3XHeZVWNdmFcovc4Jsq3V9E2kSdSTUzWH8NEuPP5MQ7XYOTx9+ruUDs2lHCjtoeo911oMZrhNpmOMVLJRsi4s4jJxe0X5DObqg4ZorOfN4yUQ+HmmeWh/hoh2oQmzpMPCX7aDuI6FL51u5LCtrdcnFmvXNTipIHV3cPJZXmdXksXG7QLLkQozsxDbtTVO7PaySbPnYIM59n0QHF1r0hdhXRR0SY/bzj9B4qBaQtPJsmMTQe/gu5rHauUjulus2R4YnzmoZpSPfeehtB9z5rcxauitLy+TIvn12M1CVimUlZIj2ssr7Z4Xmz7piavUtvVVCq8dG2rlYUOK3B7GuPUSJkLN/EV9L/Uu4b5aNom9QUki9zEO7OKKS7XqswvgDcxrNINu7Sbe16pNnzsEAy2e1xRB28KSSM503eJpu7PjJABdPZ4ogOu0QZSpvcTQ2XOxQCAuVNZou87LFDe16orPs+kkA3C/UUkguvUSd2UzLm4oCBfGkN/s5oOItEO73lsTD9pcqWgmgVp/HS3AQrNZ+c574p6gxtxvnyh8lVcDd81p4i1Azcp+89F6QIzmGbTL79680K2VSVavaauRWRW0cwzLekYOHcRMK9bNaGxGNLTMPaCDkQRMFfMLHEGYoQrq+FGm+XsroLv4kB1Otj5lpHiHjqkFUzYdUVLyi3iT1Lp+ScA3KGs0vw56U29rFY7fWsw0Rh1+hovDSFq5KG8/K0nxlT1Ww43qBcbWyPds9zNzmjwG19gpKo9U1H8ziyXTBsgVtjXWOdnKneaKNrr6ciUa3pJJ8KD6lccr6FGMhIQmF6DvanaC/FxwHfwmVf1/Kzxl5Aq2xsANAEsspZSAXD1J0eINkY2UnxfzHfuq0eDbo813mm7QrDy7nZZ+SNTHr6IfmwIuVFZou85DRdqUpVvZKqWBgX8aSSDr2ym4XsEON6gxQCLpbPFUzsYVmgGQunFDdnHNAF2W1xVAbeqkBI3sk3C9UYIBA36GkkXub4JuN6gROl3NAIm5QVmmW3aoabuKTRdqcEBwtc9WGaQszmOk2I2boT/keBn2TgR9wF89ugOhkw3gtcwlrgcQ4EhwPcZr6iIntZeypX4w6MEK2NjtEm2llf8AiQ5NcfFpZ4gq5iWal0sp5de11IgyEIWkZ4KYfCrSXI29rcozHQz0TlfafNsv3KHra0Va+RjQo3+FEhv/AJHBxHkFzZHqi0dQl0yTPpkC9U0kly56EOF6owWXLBYZtCdIbuKjOuj6QhmS4nwkB9SpNduVxUS10M3wz0tP1VnD/wAy/f8Agr5X+J/3yV/ph84pHygD7/daJWxbjOI/9R9KLXK3EZYl6QYd5waOcQPMyXmtnRxlFhnoiM/zBH2CLyawNaA3EACXdTBNsjvYouy2vGXei7ergvmjcE0z3sPJE6y5vGac79MJJXuZ6oAdTd903ACrcUTudc0Xbu1igASlM48SQ2u94ZIuz2uKI3+qSAQNZHd4lVDiRu4IvT2eKJ3rtMUAOpu+6JCU+dxkiVyuM0Xed4yQA0T3vZJpnvYJyv1wklevUwQASZyG7xOqr/43WQGxQ4jf7uO2Z6nMe363VYN6Wz696g/xjF3Rrh80WF6En7KSp+9Edy3BlJsMwmvOBgvRbS7GOCChCA+ktAWkxLLZ4g58GE451LASuldb1ea4Wo8S7o+y5zgs+i7n4frWHNak0bUHuKYmgjew81FddBtwzkWmXgaqVNN6h9lHNdWbMM5Nc4fzAH7KfEerl/fBDlL/AMmVdat9/wCp31K8FsWsSiP/AFO+pWutwywWTXSqMRXyWKYQF8QnzAfzXAEdxqFk4E1bh5LjanW3lrJCnzW3D3soJ+AB8V2XGVBgvnJx6ZOPwbUZdUUxuM932ROkudxmhwu1HuiVL2fGS5OgbTe90gCKnBNovb3skDOhwQARMzGHGSbq7vskTKgwTds7ufigAmkhjxOqGkChxQRIXhihonU4oBNEt73RKs+bxkhpvb3sidbuXGaAHCe77JuING4pON3d903CVRigAESkceM1XHxvjltjgwyavtAPgyHEn6uarHAmLxx4yVK/G7SnKWqDA/wIZcep0UgyP7WNP7lNQtzRDe9VsgUDBeiwgigWa112MlghCTjReg+idR9nR9lDsTAhkdxaCF2eTdwVraGsoZAhQzTk4UNn8rAPstnlXcBYc3uTZtRWopGRdfpguRrTDnZ3DEsLXesj6ErrulzcV52iC17HMdi5pB8RJe1y6ZqXweWR6otFJ6TbKK7rkfMBahXU0/ALXiYkatPe01+q5bl9EjGEmEkICafDjS1yI6zOMmxqtngHjEeIH9IVkB12mKoaG8gggkEEEEUIIqCD0q19UtZ4dpaGRCBHAqPnA5zPuMu5ZebQ9+ov3L+LatdD/YkQFyuM0rvPQ3tYIrPs+izi6Mi/1SQXXtlJ3ZTdLm4oADpbPFUDY65oEpV3uJIb2vBAF2W1xVBbergkJzru8SQ6fNwQDJv0wki9zfCaHdlFJdr7oABuUxmkG3a4pt7SxLrsy8yaATMmglWZ6EB4aStjIMJ9oiOushtLndzch0k4AdJXzLprST7VaItofvRnl0vlGDWg9AaAPBTH4oa7i2O/DWdx/DMcCXCgjPGBHYGXSa5BQezMrNaGPV0rb7sz8i3qel2R7gIQhXkUwXQ1esfLWqBClMPiwwR2bwL/AOkFc9Tr4PaN5S2ujETbZ4ZM+h8SbG/08ouLZdMGzuuPVJIugi/XCSf4jqSd2cFlNnUsQ2RObdqEXaXs0mtu1KLvOyQFefECwycYgFHSd4ij/sfFQkq59YrB+JguAEy2oGZpUDvEx5Km48ItcWnI/wDorbxLeutLyuDKyK+mf6mVjsj4rxDhtL3uwA888F1bVqnbYYvOgOl2Sx58mklcux2p8J7YkNxa5pmDxlKis7VPWyHaPy3i5FlgN18sS2f0+q9yLLa/dFJryKYQnxJ6ZVZHoitCCWkGbXNJDmkYOaRgVa+teqzLU0xGSZGFQ7C/0Nf75eiqqJDLSWuBDmkgg4ggyIK9pvjdHj/hzbU63pkq0J8SnQ5Qre0uGUeGKnriQ+npu+Sn+itNWe0j8iKyK3snaH6m4t8QqSiMBEiJgrnRdGEG9DcQRhUgjucKqC3CjLmPBJDKnHh8n0a43MM0Ft2oVAWXWvSdnEm2iLIfPdijzeD9VbeolrtkWzi0Wss/NkYbWsuuuZPfWW1iABhLppRtx5VrbZbqyI2PSTJIGz2s/ZDdrHJItntZeybtrCklXLAgZ7OSZN2gQTMXc/ZAddoUAOF2oRdpe8epVJ8RtZNLWG0cm2M1kKIC6E9kJky0GrHF4dtNmJyxmDSchXWk9O2q0T5a0RYoPNc91z+QbPorEMdyW9laeSovWi+NYNfbBZgb8URYgpycGT3T6HHdb4kKpNcdf7Tb5w/4MCf8JpO10co/nd1B1GU1FGQicAvZkDpVqvHUeSrZkSlx2PKHDn3LaAlQIQrUY6K7YIQhdHgFXl8LdEmBYmlwk+0HlHdIaRKGP5QD+4qqNTNAm22tkGWwNuIeiG0iY8aN/cvocSlcAlKnVRUcyzhQRdxK+XIHOu0Gay5AdaTTdoc1jyBWeXxtnzsOtFZ9n0TDr9MEXpbPhNADuz6KtviDoXk38uzcfvS5rv8AevAVkk3Oua19IWFkWG5jxea8SI78wekKfHudU9+PJFdV6kdFGFZQYhaQ5pLS0ggjEEVBC3tN6KfZophPrm12T25OH36wuctxNSW0ZLTT0y5dV9MfioDXmUxsvGQcB9DQ+K4Gveq5iE2mztvOl+YwVLpc5vS6UpjOnjwtQdLCDHMN5kyPJs8g8HYPjMjxHQrSvXaYrIs6se7cexow1dXqRQqauPSurNljbUSGC485pLHd5LcfGa5zNQbGNr80joL6eYAKtrPra5TK7xJ+NFcaGsfLR4UI4Pe0H9M5u9Jq7WDIiTRgMAOiS0NF6Ds8KsKE1hFL287+d0yuiHXqYKllZCta12Raop9NPfcRnlu8TTd2fGSL0tniqDsdc1VLAGUqb3E0NlzsUXZbXFUBt6uCAhfxY0by2j3vIm6zubFb3A3Yg7rjif2hUcyWIX0Zrib9gtYOVmjnyhOXzhZjRaGG/a0Z+YvcmeqEIV4pghCEAIQrF+FuqXKPbbYzfy2H8lp57wf4hHytOHSR1V4ssUI7Z3XBzlpEx+HWrRsVmm4fnxpOidLBLYh+EyT1k9SltJdr1QRc65ou87xksacnKTkzXhFRWkDZc7FYzf1+SYbfrhJH4jqXJ0NxvUCJ0u5ocAN3HzRISnzuMkANN3e90gJVOCba73skDOhwQHM1h0Iy2Qy11CJlj82H7g5jPyVR6R0fEgPMOI264eRGTmnMFXeTKgwXN0/oSDaYdxwqJ3XDeYerq6RmreNlOr2y7Fa+jr5XcphTPV74gCCGw7WHFmAjAXi3oERuJHaEz0jNR/Tug41kfdiCbTuvG672d1H1xXKc0ESNQVpzhC6PyihGU65cdy8dGaRgxm34UVkZpzY4O8wMCtu6Zzy4yXzhH0WQb0MyPfIjucsX2u2AXTFtEujlIhHoZKi8H4ZaWZ8o+j4jgSACATlOROdBmsnGdBiqI+G7X/2nZ3EOJ/NqQT/cxMyr3IAqMVVuq9KWtlim31I70AMhI4obs72figAGpx4yQ2u97KEmEBIzOHGSHCdRggGsjhxmm4yoMEByNcXA2C1yys0f/ScvmyzZ+C+ktcwBYLXL/wCtHnn/AHTl822bPwV/D8lDM7o90IQr5SBCRKsLUn4dvjXY9qaWQsWwqh8TrfmxnqerE8WWRgtyO4Qc3pHP+H+pTra8RYoLbO09xjEcxvZ6XeArMi8ILGsaGABoAAAAkAAJAAZBKHCaxoDAG3QAGgAAAUkGjALMAGpxWTdc7HtmpVUq1pCaLu97olW9lxkm2u97JTrLm8ZqIlBwvVasuVbwFi4y3cPNZcm3goBXblcUXZ7XjJJoI3sPNBnOfN4yQDlf6pIvXtniiHV3fZBlgMeJoAvXdniqNzrmgSFDjxJDab3ugPG02Vj2kPaHtdi0iYM1A9OahOE4llN4f4TjtD9Djvdxr1lWDIzmd3jJDgTu4eSlqvnW/ayOyqM1yURFhOY4tcC1wxBBBHeCsZq7dK6KgWlt18Nr5Zyk4dzhUKHaT+HucCLX5Ig+j2j6jxWnXm1y+rhlGzFnHtycHUV8rdBP/E/0nq3Lt3axUL1Q1QiWaNy0dzZtBDGtm6pEi5xl0E061M2zG9gqWZZGdm4vwWcWDjDn5HdntcURv9UkiDOY3eMk3V3fHJVCyF6ezxRF67TFBlKQ3uM0NkN7FAcbXJt2wWzOdmjj/lOXzdZs/BfUVtsYiwokKLO5FY5h7ntLTLrkVTjPhJbWxCBEgGHOkQucDd6Sy6SD1T8VcxbIx3tlPKrlJrSIOupoDV602192BDLhOrzSG39T8PATPUrT0J8LrLCk6K51qcMjsQp/oFT4kjqU5hQmNaGQ2tYG0DWgNAHQAKAKWzMS4gR14jf1EO1S+H1nsZD4srRHycR+XDPYYc+0a9ElNNzrmgEYHe4lVDab3hmqE5ym9yL0YRitILstriqLt7aw/wBkgDicOJIcCd3BcnQ53+qSL3N8JodXd9kUlLncZoAncpjNL8P1ptpvY+axuv6/NANrr1Ci9zcky69QURPm5oBON3DNMiVRigG5jWaQbd2uKoBgTqcUN2scki2e1xRMm/hSSAV6ezkhxu0CZdPZz9kB12hqgBwu1CJUvZpNF2prNF3nZIBtF7HJIOnQ4IIv4UkmXXqIBEy2Rgm7ZwrNIPls4nDzTGxjWaACJbWaAL1TikGy2svdBbeqKIAab2NETrdyTcb9BSSL3NzwQCcbuFUyLtRigG5Q1mkG3alAMCYvZ+yG7WNJJXZ7WXsmdvCkkAg6eycEF12gwTLp7PFEB13ZQA4XcM0SpezSaLmNZou87JANovVKx5ZyZF6opJZcuOhAEUXRMUQBszz6UIQCgi9jVJhmZHBCEAPMjIYJxtnCiEIBuGzPPpRCExM1QhAYwjeNaontSy6EIQBGN3CiyiCQmKFCEBoRrEC4PvxAYhcCA7Z/hkAgdOyOJS1ImiQ1lI0cC9CYAIhAa0xGghoAEqGVMkIQG1ocEcowuc8NiuAvOLjJoaAJnu9St6IZGQoEIQDjC7hROWzPPpQhAKEL2NUoRmZGoQhADjtSypRONsylRCEA3iQmMUQxMTNShCAxg7WNUT2pZdCEIAimRkKL1EIdCSEB/9k=",
                        isPrivate = true,
                        visibility = Repo.Visibility.Public,
                        htmlUrl = "https://github.com/octocat",
                    )
                }
            ),
            onRepoClick = { println("onRepoClick") },
            onLoadMore = { println("onLoadMore") }
        )
    }
}
