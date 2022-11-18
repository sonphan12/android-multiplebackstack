package io.github.sonphan12.myplayground

import androidx.fragment.app.Fragment

class FragmentFactoryWithTagString(
    val fragmentFactory: () -> Fragment,
    val tag: String
)
